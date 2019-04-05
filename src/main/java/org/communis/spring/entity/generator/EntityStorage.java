package org.communis.spring.entity.generator;

import com.github.vastik.spring.extensions.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityStorage {
    private Set<Class<?>> entityClasses = new HashSet<>();

    @Autowired
    private ApplicationContext applicationContext;

    public EntityStorage(List<String> basePackages) {
        basePackages.forEach(p -> {
            Reflections reflections = new Reflections(p);
            entityClasses.addAll(reflections.getTypesAnnotatedWith(Entity.class));
        });
    }

    public void putClass(Class<?> cl) {
        entityClasses.add(cl);
    }

    Class<?> getClassByName(String name) {
        Optional<Class<?>> optionalClass = entityClasses.stream()
                .filter(cl -> cl.getSimpleName().equals(name))
                .findAny();

        if (!optionalClass.isPresent())
            throw new NotFoundException("Entity with className %s not found", name);

        return optionalClass.get();
    }

    JpaRepository getRepositoryForClass(Class cl) {
        JpaRepository repository = null;

        try {
            String simpleName = cl.getSimpleName();
            String uncapitalize = StringUtils.uncapitalize(simpleName);
            return applicationContext.getBean(uncapitalize + "Repository", JpaRepository.class);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to get repository class for entity %s", cl.getSimpleName());
        }
    }

    boolean isEntity(Class<?> cl) {
        return entityClasses.stream().anyMatch(c -> c.equals(cl));
    }

    List<String> getAvailableNames() {
        return entityClasses.stream().map(Class::getSimpleName).collect(Collectors.toList());
    }
}
