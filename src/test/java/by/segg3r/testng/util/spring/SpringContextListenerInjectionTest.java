package by.segg3r.testng.util.spring;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.isSpy;
import static org.testng.Assert.*;

import by.segg3r.testng.util.spring.extension.SpringContextListenerInjectionParent;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import by.segg3r.testng.util.spring.annotations.Mocked;
import by.segg3r.testng.util.spring.annotations.Real;
import by.segg3r.testng.util.spring.annotations.Spied;

@Listeners(SpringContextListener.class)
public class SpringContextListenerInjectionTest extends SpringContextListenerInjectionParent {

	@Real
	private Service service;
	@Spied
	private SpiedDao spiedDao;
	@Mocked
	private MockedDao mockedDao;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Test(description = "should correctly configure ApplicationContext from custom directives and inject fields")
	public void testApplicationContextConfigurationAndInjection() {
		assertTrue(service != null && !isMock(service) && !isSpy(service));
		assertTrue(isSpy(spiedDao));
		assertTrue(isMock(mockedDao));
		
		assertEquals(spiedDao, service.spiedDao);
		assertEquals(mockedDao, service.mockedDao);
	}

	@Test(description = "should correctly inject fields into parent classes")
	public void testParentClassInjection() {
		assertTrue(parentService != null && !isMock(parentService) && !isSpy(parentService));
		assertTrue(isSpy(parentSpiedService));
		assertTrue(isMock(parentMockedService));

		assertEquals(parentApplicationContext, applicationContext);
	}

	@Test(description = "should correctly inject application context")
	public void testApplicationContextInjection() {
		assertEquals(applicationContext.getBean(SpiedDao.class), spiedDao);
	}
	
	@Test(description = "should have been called once")
	public void testShouldHaveBeenCalledOnce() {
		mockedDao.call();
		verify(mockedDao).call();
	}
	
	@Test(description = "should have been reset")
	public void testShouldHaveBeenReset() {
		mockedDao.call();
		verify(mockedDao, times(1)).call();
	}
	
	public static class Service {
		@Autowired
		private SpiedDao spiedDao;
		@Autowired
		private MockedDao mockedDao;
	}
	
	public static class SpiedDao {
	}

	public static class MockedDao {
		public void call() {}
	}
	
}
