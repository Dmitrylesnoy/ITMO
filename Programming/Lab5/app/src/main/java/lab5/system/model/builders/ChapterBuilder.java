package lab5.system.model.builders;

import lab5.system.io.Console.StdConsole;
import lab5.system.model.Chapter;

public class ChapterBuilder {
    ;
    private String name; // Поле не может быть null, Строка не может быть пустой
    private String world; // Поле может быть null

    public ChapterBuilder() {
        setName();
        setWorld();
    }

    public Chapter build() {
        return new Chapter(name, world);
    }

    public void setName() {
        while (true) {
            StdConsole.write("Enter the Name of the Chapter: ");
            name = StdConsole.read();
            if (name != null && !name.isEmpty()) {
                this.name = name;
                break;
            } else {
                StdConsole.writeln("Invalid input: Name cannot be null or empty. Please try again.");
            }
        }
    }

    public void setWorld() {
        while (true) {
            StdConsole.write("Enter the World of the Chapter: ");
            world = StdConsole.read();
            if (world != null) {
                this.world = world;
                break;
            } else {
                StdConsole.writeln("Invalid input: World cannot be null. Please try again.");
            }
        }
    }
}
