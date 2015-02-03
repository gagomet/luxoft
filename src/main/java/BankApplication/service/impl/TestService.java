package BankApplication.service.impl;

import BankApplication.annotation.NoDB;
import BankApplication.service.Persistable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Kir Kolesnikov on 30.01.2015.
 */
public class TestService {
    /**
     * Данный метод должен анализировать поля классов o1 и o2
     * Он должен сравнивать все поля с помощью equals,
     * за исключением тех полей, которые помечены аннотацией
     *
     * @NoDB и возвращать true, если все поля совпали.
     * также он должен уметь сравнивать коллекции.
     */
    public static boolean isEquals(Object o1, Object o2) {
        if (!Persistable.class.isAssignableFrom(o1.getClass()) || !Persistable.class.isAssignableFrom(o2.getClass())) {
            return o1.equals(o2);
        } else {

            if (o1 == o2) {
                return true;
            }

            if (o1.getClass().getCanonicalName().equals(o2.getClass().getCanonicalName())) {
                Field[] fields01 = o1.getClass().getDeclaredFields();
                Field[] fields02 = o2.getClass().getDeclaredFields();

                for (int i = 0; i < fields01.length; i++) {
                    Field field01 = fields01[i];
                    Field field02 = fields02[i];
                    if (!field01.isAnnotationPresent(NoDB.class)) {
                        field01.setAccessible(true);
                        field02.setAccessible(true);
                        try {
                            if (Collection.class.isAssignableFrom(field01.getType())) {
                                Collection collectionFieldFromObject1 = (Collection) field01.get(o1);
                                Collection collectionFieldFromObject2 = (Collection) field02.get(o2);
                                if (collectionFieldFromObject1.size() != collectionFieldFromObject2.size()) {
                                    return false;
                                }
                                Iterator iterator1 = collectionFieldFromObject1.iterator();
                                Iterator iterator2 = collectionFieldFromObject2.iterator();
                                while (iterator1.hasNext()) {
                                    isEquals(iterator1.next(), iterator2.next());
                                }
                            } else {
                                Object value01 = field01.get(o1);
                                Object value02 = field02.get(o2);
                                if (!value01.equals(value02)) {
                                    return false;
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
    }
}
