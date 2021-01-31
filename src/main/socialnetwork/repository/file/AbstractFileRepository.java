package main.socialnetwork.repository.file;

import main.socialnetwork.domain.Entity;
import main.socialnetwork.domain.validators.Validator;
import main.socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


///Aceasta clasa implementeaza sablonul de proiectare Template Method
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                if(!line.matches(""))
                {E entity = extractEntity(Arrays.asList(line.split(";")));
                super.add(entity);}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * extract entity - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes a list containing all the attributes needed for the entity as strings
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * create entity as string - template method design pattern
     * creates a string containing the attributes of the given entity
     *
     * @param entity the entity for which we create the string
     * @return a string with the attributes of the entity
     */
    protected abstract String createEntityAsString(E entity);

    @Override
    public Optional<E> add(E entity) {
        Optional<E> added = super.add(entity);
        if (added.isEmpty())
            writeToFile(entity);
        return added;
    }

    @Override
    public Optional<E> remove(ID id) {
        Optional<E> ent = super.remove(id);
        if (ent.isPresent())
            updateFile();
        return ent;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> ent = super.update(entity);
        if (ent.isEmpty())
            updateFile();
        return ent;
    }

    protected void writeToFile(E entity) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            bufferedWriter.write(createEntityAsString(entity));
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void updateFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false))) {
            bufferedWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (E e : entities.values())
                writeToFile(e);
    }
}

