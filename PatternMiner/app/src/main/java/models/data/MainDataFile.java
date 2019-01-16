package models.data;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainDataFile extends DataFile {
    public final static String DIR_PATH = "/resources/datasets/input/csv/all/";

    public MainDataFile(String realPath) {
        super(realPath);
    }

    public void splitIntoSortedGroups() {
        MainToGroupConverter converter = new MainToGroupConverter();
        converter.splitIntoSortedGroups(this);
        try {
            FileUtils.writeStringToFile(this, "SPLIT INTO GROUP-FILES > " + getPathToChildren(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
