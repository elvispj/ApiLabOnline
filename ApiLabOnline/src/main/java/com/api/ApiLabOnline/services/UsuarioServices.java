package com.api.ApiLabOnline.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.api.ApiLabOnline.entity.Perfil;
import com.api.ApiLabOnline.entity.Usuario;
import com.api.ApiLabOnline.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServices {
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PerfilServices perfilRepository;
	
	public User getUser(String usuariocorreo) {
		Usuario usuario = usuarioRepository.findByUsuariocorreo(usuariocorreo);
		if(usuario==null)
			return null;
	    List<GrantedAuthority> authorities = buildUserAuthority(perfilRepository.findByPerfilid(usuario.getPerfilid()));
		return buildUserForAuthentication(usuario,authorities);
	}
	
	public Usuario findByUsuariocorreo(String usuariocorreo) {
		return usuarioRepository.findByUsuariocorreo(usuariocorreo);
	}
	
	public Usuario findByUsuarioid(int usuarioid) {
		return usuarioRepository.findByUsuarioid(usuarioid);
	}

	public Usuario save(Usuario user) {
		return usuarioRepository.save(user);
	}

	private List<GrantedAuthority> buildUserAuthority(Perfil perfil){//Set<Perfil> perfil) {
	    Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>(); 
//	    for(UserRole userRole  : userRoles){
	        log.info("called buildUserAuthority(role) method....."+perfil.getPerfilnombre());
//	        setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
//	    }
	    setAuths.add(new SimpleGrantedAuthority(perfil.getPerfilnombre()));
	    List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(setAuths);
	    return grantedAuthorities;
	}
	
	private User buildUserForAuthentication(Usuario user, List<GrantedAuthority> authorities) {
	    //accountNonExpired, credentialsNonExpired, accountNonLocked, authorities properties
		log.info("called buildUserForAuthentication(Users user, List<GrantedAuthority> authorities) method....");
		log.info(user.getUsuariocorreo()+" - "+ user.getUsuariopwd()+ " - " + user.isUsuarioactivo());
	    return new User(user.getUsuariocorreo(), user.getUsuariopwd(), user.isUsuarioactivo(), true, true, true, authorities);
	}
	
}
