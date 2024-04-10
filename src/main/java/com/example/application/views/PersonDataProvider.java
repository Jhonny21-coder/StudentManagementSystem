package com.example.application.views;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.example.application.services.StudentService;
import com.example.application.data.Student;
import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import static java.util.Comparator.naturalOrder;

// Person data provider
public class PersonDataProvider extends AbstractBackEndDataProvider<Student, CrudFilter> {
    //private StudentService studentService = null;

    /*public PersonDataProvider(StudentService studentService){
	this.studentService = studentService;
    }*/

    // A real app should hook up something like JPA
    final List<Student> DATABASE = new ArrayList<>(StudentService.getAllStudents());

    private Consumer<Long> sizeChangeListener;

    @Override
    protected Stream<Student> fetchFromBackEnd(Query<Student, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<Student> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream.filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<Student, CrudFilter> query) {
        // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    private static Predicate<Student> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<Student>) student -> {
                    try {
                        Object value = valueOf(constraint.getKey(), student);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }).reduce(Predicate::and).orElse(e -> true);
    }

    private static Comparator<Student> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream().map(sortClause -> {
            try {
                Comparator<Student> comparator = Comparator.comparing(
                        student -> (Comparable) valueOf(sortClause.getKey(),
                                student));

                if (sortClause.getValue() == SortDirection.DESCENDING) {
                    comparator = comparator.reversed();
                }

                return comparator;

            } catch (Exception ex) {
                return (Comparator<Student>) (o1, o2) -> 0;
            }
        }).reduce(Comparator::thenComparing).orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, Student student) {
        try {
            Field field = Student.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(student);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void persist(Student item) {
        if (item.getId() == null) {
            item.setId(DATABASE.stream().map(Student::getId).max(naturalOrder())
                    .orElse(0L) + 1L);
        }

        final Optional<Student> existingItem = find(item.getId());
        if (existingItem.isPresent()) {
            int position = DATABASE.indexOf(existingItem.get());
            DATABASE.remove(existingItem.get());
            DATABASE.add(position, item);
        } else {
            DATABASE.add(item);
        }
    }

    Optional<Student> find(Long id) {
        return DATABASE.stream().filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    void delete(Student item) {
        DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
    }
}
