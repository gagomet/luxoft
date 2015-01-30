package BankApplication.service.impl;

import BankApplication.annotation.NoDB;

import java.lang.annotation.Annotation;
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
        Class instanceO1 = o1.getClass();
        Class instance02 = o2.getClass();
        if (instanceO1.getCanonicalName().equals(instance02.getCanonicalName())) {
            Field[] fields01 = instanceO1.getDeclaredFields();
            Field[] fields02 = instance02.getDeclaredFields();
            for (int i = 0; i < fields01.length; i++) {
                Field field01 = fields01[i];
                Field field02 = fields02[i];
                if (field01.isAnnotationPresent(NoDB.class)) {
//TODO finish this method
                } else {
                    try {
                        Object field01Value = field01.get(field01.getType());
                        Object field02Value = field02.get(field01.getType());
                        if (!field01Value.equals(field02Value)) {
                            return false;
                        } else {

                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return false;
    }

    private boolean isFieldsEquals(){
        //TODO
        return false;
    }


}
