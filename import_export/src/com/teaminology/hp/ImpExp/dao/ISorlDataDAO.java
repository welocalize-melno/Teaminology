package com.teaminology.hp.ImpExp.dao;

import java.util.List;

import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermInformationBo;

public interface ISorlDataDAO {

	public List<TermInformationBo> getTermBaseData(Integer companyId,Integer recordIndex,Integer maxRecordPerBatch,
			Integer startPK,Integer endPK);

	public List<String> getTermBaseIds();


}
