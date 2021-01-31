package main.socialnetwork.repository.memory;

import main.socialnetwork.domain.Entity;
import main.socialnetwork.domain.validators.Validator;
import main.socialnetwork.repository.Repository;
import main.socialnetwork.repository.RepositoryException;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;
    protected HashMap<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null)
            throw new RepositoryException("ID-ul nu poate fi null!");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> add(E entity) {
        validator.validate(entity);

        if (entities.get(entity.getId()) != null)
            return Optional.of(entity);

        entities.put(entity.getId(), entity);
        return Optional.empty();
    }

    @Override
    public Optional<E> remove(ID id) {
        if (id == null)
            throw new RepositoryException("ID-ul nu poate fi null!");

        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        validator.validate(entity);

        entities.put(entity.getId(), entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }

        return Optional.of(entity);
    }
}
