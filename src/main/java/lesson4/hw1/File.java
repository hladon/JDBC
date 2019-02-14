package lesson4.hw1;

public class File {
    private long id;
    private String name;
    private String format;
    private long size;
    Storage storage;

    public File(long id, String name, String format, long size, Storage storage) {
        this.id = id;
        this.name = name;
        this.format = format;
        this.size = size;
        this.storage = storage;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public long getSize() {
        return size;
    }

    public Storage getStorage() {
        return storage;
    }
}
