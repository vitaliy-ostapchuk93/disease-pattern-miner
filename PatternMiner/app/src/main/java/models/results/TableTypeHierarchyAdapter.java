package models.results;

import com.google.common.collect.Table;
import com.google.gson.*;
import models.data.GenderAgeGroup;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class TableTypeHierarchyAdapter<R extends String, C extends GenderAgeGroup, V extends ResultsEntry> implements JsonSerializer<Table<R, C, V>> {

    @Override
    public JsonElement serialize(Table<R, C, V> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray rowKeysJsonArray = new JsonArray();
        Map<R, Integer> rowKeyToIndex = new HashMap<>();
        for (R rowKey : src.rowKeySet()) {
            rowKeyToIndex.put(rowKey, rowKeyToIndex.size());
            rowKeysJsonArray.add(context.serialize(rowKey));
        }
        JsonArray columnKeysJsonArray = new JsonArray();
        Map<C, Integer> columnKeyToIndex = new HashMap<>();
        for (C columnKey : src.columnKeySet()) {
            columnKeyToIndex.put(columnKey, columnKeyToIndex.size());
            columnKeysJsonArray.add(context.serialize(columnKey.toString()));
        }
        JsonArray cellsJsonArray = new JsonArray();
        for (Table.Cell<R, C, V> cell : src.cellSet()) {
            JsonObject cellJsonObject = new JsonObject();
            int rowIndex = rowKeyToIndex.get(cell.getRowKey());
            int columnIndex = columnKeyToIndex.get(cell.getColumnKey());
            cellJsonObject.addProperty("rowIndex", rowIndex);
            cellJsonObject.addProperty("columnIndex", columnIndex);
            cellJsonObject.add("value", context.serialize(cell.getValue().getRelSupportValue()));
            cellsJsonArray.add(cellJsonObject);
        }
        JsonObject tableJsonObject = new JsonObject();
        tableJsonObject.add("rowKeys", rowKeysJsonArray);
        tableJsonObject.add("columnKeys", columnKeysJsonArray);
        tableJsonObject.add("cells", cellsJsonArray);
        return tableJsonObject;
    }
}