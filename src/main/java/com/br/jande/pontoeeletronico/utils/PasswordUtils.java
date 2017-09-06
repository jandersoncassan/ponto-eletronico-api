package com.br.jande.pontoeeletronico.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	private static final Logger LOG = LoggerFactory.getLogger(PasswordUtils.class);
	
	/**
	 * Método responsavel por gerar a senha cryptografada BCrypt
	 * @param senha
	 * @return
	 */
	public static String gerarBCrypt(String senha) {
		if(senha == null) {
			return senha;
		}
		LOG.info("Gerando senha com o BCrypt");
		BCryptPasswordEncoder bCryptEnconder = new BCryptPasswordEncoder();
		return bCryptEnconder.encode(senha);
	}
}
