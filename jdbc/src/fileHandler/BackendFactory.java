package fileHandler;

import dbms.DatabaseException;
import fileHandler.json.JSONReader;
import fileHandler.json.JSONWriter;
import fileHandler.xml.XMLReader;
import fileHandler.xml.XMLWriter;

public class BackendFactory {
    private String backendType;

    public BackendFactory(String backendType) {
        this.backendType = backendType;

    }

    public IFileReader createReader() throws Exception {

        switch (this.backendType) {
            case "xml":
                return new XMLReader();
            case "json":
            	return new JSONReader();
            default:
                throw new DatabaseException("Unsupported File Format");
        }

    }

    public IFileWriter createWriter() throws Exception {
        switch (this.backendType) {
            case "xml":
                return new XMLWriter();
            case "json":
            	return new JSONWriter();
            default:
                throw new DatabaseException("Unsupported File Format");
        }
    }

}
