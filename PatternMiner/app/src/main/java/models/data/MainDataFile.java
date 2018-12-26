package models.data;

public class MainDataFile extends DataFile {
    public final static String DIR_PATH = "/resources/datasets/input/csv/all/";

    public MainDataFile(String realPath) {
        super(realPath);
    }

    public void splitIntoSortedGroups() {
        MainToGroupConverter converter = new MainToGroupConverter();
        converter.splitIntoSortedGroups(this);
    }


    @Override
    public String getPathToChildren() {
        return GroupDataFile.DIR_PATH;
    }

    @Override
    public DataFile createChildFile(String path) {
        return new GroupDataFile(path);
    }

}
