package cl.christian.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cl.christian.demo.util.LoginSuccessMessage;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder passEnconder;
	
	@Autowired
	private LoginSuccessMessage successMessage;
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/index","/listarPublicidad", "/listacarteles", "detalle/**", "/nombre/detalle/**","/listacarteles","/detallecartel/**","/recursos/**").permitAll()
		.antMatchers("/listar","/new","/nuevocartel","/agregarCartel","/inicioUsuario").hasAnyRole("USER")
		.antMatchers("/savecampania").hasAnyRole("USER")
		.antMatchers("modificarcampania/**").hasAnyRole("USER")
		.antMatchers("detalle/**").hasAnyRole("USER")
		.antMatchers("/eliminarcampania/**").hasAnyRole("USER")
		.antMatchers("/savecartel").hasAnyRole("USER")
		.antMatchers("modificarcartel/**").hasAnyRole("USER")
		.antMatchers("/eliminarcartel/**").hasAnyRole("USER")
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.successHandler(successMessage)
		.loginPage("/login")
		.permitAll()
		.and()
		.logout().permitAll();
	}




	@Autowired
	public void configurerSecurityGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passEnconder)
		.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
		.authoritiesByUsernameQuery("SELECT u.username, r.rol FROM roles r INNER JOIN users u ON r.user_id=u.idusuario WHERE u.username=?");
		
	}
}