package by.segg3r.testng.util.spring;

import by.segg3r.testng.util.spring.annotations.Mocked;
import by.segg3r.testng.util.spring.annotations.Real;
import by.segg3r.testng.util.spring.annotations.Spied;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.isSpy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Listeners({SpringContextListener.class})
public class SpringContextListenerQualifierTest {

	@Real
	private QualifierTestService service;
	@Real @Qualifier("realBean")
	private QualifierTestBean realBean;
	@Spied @Qualifier("spiedBean")
	private QualifierTestBean spiedBean;
	@Mocked @Qualifier("mockedBean")
	private QualifierTestBean mockedBean;

	public static class QualifierTestService {
		@Autowired @Qualifier("realBean")
		public QualifierTestBean realBean;
		@Autowired @Qualifier("spiedBean")
		public QualifierTestBean spiedBean;
		@Autowired @Qualifier("mockedBean")
		public QualifierTestBean mockedBean;
	}

	public static class QualifierTestBean {}

	@Test(description = "should register multiple bean with different qualifiers")
	public void testQualifiers() {
		assertEquals(service.realBean, realBean);
		assertFalse(isSpy(service.realBean));
		assertFalse(isMock(service.realBean));

		assertEquals(service.spiedBean, spiedBean);
		assertTrue(isSpy(service.spiedBean));

		assertEquals(service.mockedBean, mockedBean);
		assertTrue(isMock(service.mockedBean));
	}

}
