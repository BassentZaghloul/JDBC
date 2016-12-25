package fileHandler.protocolBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fileHandler.IFileWriter;
import fileHandler.protocolBuffer.TableProto.Column;
import fileHandler.protocolBuffer.TableProto.Table;
import fileHandler.protocolBuffer.TableProto.Table.Builder;

public class ProtocolWriter implements IFileWriter {
    private int index;
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    private String tableName;
    private String tablePath;
    private String extension;
    private FileOutputStream outputFile;
    private ArrayList<String> columnNames;
    private ArrayList<String> columnTypes;
    
    public ProtocolWriter() {
        extension = ".ser";
        
    }
    
    @Override
    public void initializeWriter(String tableName, String tablePath) throws Exception {
        this.tableName = tableName;
        this.tablePath = tablePath;
        try {
            outputFile = new FileOutputStream(tablePath);
        } catch (Exception e) {
            throw new ProtocolBufferException("An error occured while creating table file");
        }
        
    }
    
    @Override
    public void endWriter(String tableName) throws Exception {
        outputFile.close();
        
    }
    
    @Override
    public void createTableIdentifier(String tableName, ArrayList<String> columnNames, ArrayList<String> columnTypes)
            throws Exception {
        try {
            this.columnNames = columnNames;
            this.columnTypes = columnTypes;
            Column column;
            Builder tableBuilder;
            Table table;
            tableBuilder = TableProto.Table.newBuilder();
            tableBuilder.setTablename(tableName);
            for (int i = 0; i < columnNames.size(); i++) {
                column = TableProto.Column.newBuilder().setColname(columnNames.get(i)).setColtype(columnTypes.get(i))
                        .build();
                tableBuilder.addCol(column);
            }
            table = tableBuilder.build();
            table.writeTo(outputFile);
            outputFile = new FileOutputStream(this.tablePath);
            outputFile.close();
        } catch (Exception e) {
            throw new ProtocolBufferException("error in creating table identifier");
        }
        
    }
    
    @Override
    public void writeRow(ArrayList<String> columnNames, ArrayList<String> columnValues) throws Exception {
        try {
            outputFile = new FileOutputStream(this.tablePath, true);
            Column column;
            Table table = Table.parseFrom(new FileInputStream(tablePath));
            Builder tableBuilder = table.toBuilder();
            List list = table.getColList();
            
            for (int i = 0; i < columnNames.size(); i++) {
                column = (Column) list.get(i); //old column
                if (index >= column.getValuesCount()) { // inserting new value
                    if (columnValues.get(i) != null) {
                        column = TableProto.Column.newBuilder(column).addValues(columnValues.get(i)).build();
                    } else {
                        column = TableProto.Column.newBuilder(column).addValues("\0").build();
                    }
                } else { // updating a value
                    column = TableProto.Column.newBuilder(column).setValues(index, columnValues.get(i)).build();
                }
                tableBuilder.setCol(i, column);
            }
            table = tableBuilder.build();
            outputFile = new FileOutputStream(this.tablePath);
            table.writeTo(outputFile);
            outputFile.close();
            
        } catch (Exception e) {
            throw new ProtocolBufferException("Error in adding new row");
        }
        
    }
    
    @Override
    public void copyFile(File source, File dest) throws IOException {
        
    }
    
    //    @Override
    //    public void clearTable() throws Exception {
    //        initializeWriter(this.tableName, this.tablePath);
    //        createTableIdentifier(this.tableName, this.columnNames, this.columnTypes);
    //        
    //    }
    
}
