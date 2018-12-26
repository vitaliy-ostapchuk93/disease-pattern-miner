package models.results;

import models.data.ICDCode;

public class ICDLink implements Comparable<ICDLink> {

    private ICDCode nodeSource;
    private ICDCode nodeTarget;
    private int count;

    public ICDLink(ICDCode nodeSource, ICDCode nodeTo) {
        this.nodeSource = nodeSource;
        this.nodeTarget = nodeTo;
        this.count = 0;
    }


    public int getCount() {
        return count;
    }

    public void addCount() {
        this.count = this.count + 1;
    }

    @Override
    public int compareTo(ICDLink obj) {
        return Integer.compare(this.count, obj.count);
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
