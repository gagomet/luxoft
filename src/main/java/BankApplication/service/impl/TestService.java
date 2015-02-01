package BankApplication.service.impl;

import BankApplication.annotation.NoDB;

import java.lang.reflect.Field;

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
        Class instance01 = o1.getClass();
        Class instance02 = o2.getClass();

        if (instance01.getCanonicalName().equals(instance02.getCanonicalName())) {
            Field[] fields01 = instance01.getDeclaredFields();
            Field[] fields02 = instance02.getDeclaredFields();

            for (int i = 0; i < fields01.length; i++) {
                Field field01 = fields01[i];
                Field field02 = fields02[i];
                if (!field01.isAnnotationPresent(NoDB.class) && !field02.isAnnotationPresent(NoDB.class)) {
                    field01.setAccessible(true);
                    field02.setAccessible(true);
                    Class typeField01 = field01.getType();
                    Class typeField02 = field02.getType();
                    try {
                        Object value01 = field01.get(o1);
                        Object value02 = field02.get(o1);
                        if (!value01.equals(value02)) {
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean isFieldsEquals(Field field1, Field field2) {


        return false;
    }


}
