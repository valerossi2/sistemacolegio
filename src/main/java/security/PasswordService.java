package security;
import org.mindrot.jbcrypt.BCrypt;

public final class PasswordService {

    private static final int WORK_FACTOR =12;
    private PasswordService (){}

    public static String hash (String PlainPassword){
        if (PlainPassword == null || PlainPassword.isBlank())
            throw new IllegalArgumentException("la contraseña no puede estar vacia");
        return BCrypt.hashpw(PlainPassword, BCrypt.gensalt(WORK_FACTOR));
    }

    public static Boolean verify (String PlainPassword, String StoredHash){
        if (PlainPassword == null || StoredHash == null) return false;

        try{
            return BCrypt.checkpw(PlainPassword, StoredHash);
        }catch (Exception e){
            return false;
        }
    }
}
