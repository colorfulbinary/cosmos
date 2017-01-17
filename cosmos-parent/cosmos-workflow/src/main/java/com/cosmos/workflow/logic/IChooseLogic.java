package com.cosmos.workflow.logic;

import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public interface IChooseLogic {
	public boolean check(final ISequenceLogicData payload);
}
