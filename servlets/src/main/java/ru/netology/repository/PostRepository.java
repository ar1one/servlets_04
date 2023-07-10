package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
public class PostRepository {
    private ConcurrentHashMap<Long, Post> rep = new ConcurrentHashMap<>();
    private AtomicLong count = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(rep.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.of(rep.get(id));
    }


    /*Если от клиента приходит пост с id=0, значит, это создание нового поста.
    Вы сохраняете его в списке и присваиваете ему новый id. Достаточно хранить
    счётчик с целым числом и увеличивать на 1 при создании каждого нового поста.

  Если от клиента приходит пост с id !=0, значит, это сохранение (обновление)
  существующего поста. Вы ищете его в списке по id и обновляете. Продумайте самостоятельно,
  что вы будете делать, если поста с таким id не оказалось: здесь могут быть разные стратегии.
     */

    public synchronized Post save(Post post) {
        if (post.getId() == 0) {
            if (count.get() == 0) {
                rep.put(count.incrementAndGet(), post);
            } else {
                post.setId(count.get());
                rep.put(count.incrementAndGet(), post);
            }
        } else {
            long postCount = post.getId();
            if (rep.containsKey(postCount)) {
                rep.put(postCount+1, post);
            } else {
                throw new NotFoundException("Что то пошло не так...");
            }
        }
        return post;
    }

    public void removeById(long id) {
        rep.remove(id);
    }
}
