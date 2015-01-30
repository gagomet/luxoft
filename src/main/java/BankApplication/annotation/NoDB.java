package BankApplication.annotation;

/**
 * Created by Kir Kolesnikov on 30.01.2015.
 */
public @interface NoDB {
    public boolean ignore() default true;
    //TODO instantiate for reflection
}
