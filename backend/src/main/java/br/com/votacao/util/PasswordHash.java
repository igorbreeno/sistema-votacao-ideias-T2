package br.com.votacao.util;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
public final class PasswordHash {
 public static String hash(String senha) {
  byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
  PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(),salt,600000,256);
  try { return "pbkdf2$600000$"+Base64.getEncoder().encodeToString(salt)+"$"+Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded()); }
  catch (java.security.GeneralSecurityException e) { throw new IllegalStateException(e); } finally { spec.clearPassword(); }
 }
}
