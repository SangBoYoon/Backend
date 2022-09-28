package com.sangboyoon.accounter.advice;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;
import com.sangboyoon.accounter.advice.ValidationGroups.*;

@GroupSequence({Default.class, NotEmptyGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {
}
