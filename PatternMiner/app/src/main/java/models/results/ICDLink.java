package models.results;

import models.data.ICDCode;

import java.io.Serializable;

public class ICDLink implements Serializable {

    private ICDCode nodeSource;
    private ICDCode nodeTarget;

    public ICDLink(ICDCode nodeSource, ICDCode nodeTo) {
        this.nodeSource = nodeSource;
        this.nodeTarget = nodeTo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ICDLink)) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        ICDLink icdLink = (ICDLink) obj;
        return (this.nodeSource.equals(icdLink.nodeSource) && this.nodeTarget.equals(icdLink.nodeTarget));

    }

    @Override
    public int hashCode() {
        return (nodeSource.getSmallCode() + nodeTarget.getSmallCode()).hashCode();
    }


    public ICDCode getSource() {
        return nodeSource;
    }

    public ICDCode getTarget() {
        return nodeTarget;
    }

}
