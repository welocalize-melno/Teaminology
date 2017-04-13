package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.List;

public class GsListObject implements Serializable
{
    private List<GlobalsightTermInfoTO> globalsightTermInfoTOList;

    public List<GlobalsightTermInfoTO> getGlobalsightTermInfoTOList() {
        return globalsightTermInfoTOList;
    }

    public void setGlobalsightTermInfoTOList(
            List<GlobalsightTermInfoTO> globalsightTermInfoTOList) {
        this.globalsightTermInfoTOList = globalsightTermInfoTOList;
    }


}
