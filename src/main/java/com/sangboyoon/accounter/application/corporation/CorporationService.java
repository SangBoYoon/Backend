package com.sangboyoon.accounter.application.corporation;

import com.sangboyoon.accounter.application.XMLParsing;
import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.corporation.CorporationRepository;
import com.sangboyoon.accounter.domain.corporation.LikeEntity;
import com.sangboyoon.accounter.domain.corporation.LikeRepository;
import com.sangboyoon.accounter.domain.corporation.exception.CCorporationNotFoundException;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.web.corporation.dto.CorporationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CorporationService implements CorporationUseCase {

    private final CorporationRepository corporationRepository;
    private final LikeRepository likeRepository;
    private final XMLParsing xmlParsing = new XMLParsing();

    @Override
    public List<Corporation> findAllCorporation() {
        return corporationRepository.findAll();
    }

    @Override
    public CorporationDto findCorporation(String corpCode) {
        Corporation corporation = corporationRepository.findByCorpCode(corpCode).orElseThrow(RuntimeException::new);
        return new CorporationDto(corporation);
    }

    @Transactional
    @Override
    public int saveLike(String corpCode, User user) {
        Corporation corporation = corporationRepository.findByCorpCode(corpCode).orElseThrow(CCorporationNotFoundException::new);
        Optional<LikeEntity> findLike = likeRepository.findByCorporationAndUserId(corpCode, user.getId());

        if(findLike.isEmpty()) {
            LikeEntity likeEntity = LikeEntity.toLikeEntity(user, corporation);
            likeRepository.save(likeEntity);
            corporationRepository.plusLike(corporation.getCorpCode());
            return 1;
        } else {
            likeRepository.deleteByCorporationAndUserIdInQuery(corporation.getCorpCode(), user.getId());
            corporationRepository.minusLike(corporation.getCorpCode());
            return 0;
        }
    }

    @Override
    public void startQuery() {
        List<List<String>> list = readCSV();

        List<List<String>> corp_code_list = xmlParsing.getXML();

        for(int i = 1; i < list.toArray().length; i++) {
            String corpCode = "";
            for(int j = 0; j < corp_code_list.toArray().length; j++) {
                if(corp_code_list.get(j).get(1).equals(list.get(i).get(1).replaceAll("\"", ""))) {
                    corpCode = corp_code_list.get(j).get(0);
                }
            }
            if(corpCode == "") {
                continue;
            }

            System.out.println(list.get(i).get(1) + list.get(i).get(3) + corpCode);

            corporationRepository.addCorp(corpCode, list.get(i).get(1).replaceAll("\"", ""), list.get(i).get(3).replaceAll("\"", ""));
        }
    }

    @Override
    public List<List<String>> readCSV() {
        List<List<String>> csvList = new ArrayList<List<String>>();

        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/corpCode.csv")));

            while ((line = br.readLine()) != null) {
                List<String> aLine = new ArrayList<String>();
                String[] lineArr = line.split(",");
                aLine = Arrays.asList(lineArr);
                csvList.add(aLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();;
        } catch (IOException e) {
            e.printStackTrace();;
        } finally {
            try {
                if(br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvList;
    }
}
