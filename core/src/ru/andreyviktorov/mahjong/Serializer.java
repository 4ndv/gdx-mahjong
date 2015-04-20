package ru.andreyviktorov.mahjong;

import com.badlogic.gdx.utils.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {
    public static Object fromString( String s ) {
        try {
            byte [] data = Base64Coder.decode(s);
            Object o = null;
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(data));
            o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString( Serializable o ) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( o );
            oos.close();
            return new String( Base64Coder.encode( baos.toByteArray() ) );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
