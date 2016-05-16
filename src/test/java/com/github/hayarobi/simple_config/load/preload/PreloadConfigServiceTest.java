package com.github.hayarobi.simple_config.load.preload;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.hayarobi.simple_config.load.ConfigLoader;
import com.github.hayarobi.simple_config.sample.DataConfig;
import com.github.hayarobi.simple_config.sample.ListAndDateConfig;
import com.github.hayarobi.simple_config.sample.MapAndAbstractionConfig;
import com.github.hayarobi.simple_config.sample.OtherConfig;

public class PreloadConfigServiceTest {

	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testInit() {
		ConfigClassScanner mockClassScanner = mock(ConfigClassScanner.class);
		ConfigLoader mockLoader = mock(ConfigLoader.class);	
		String[] scanPackages = {"com.github.hayarobi", "org.eclipse.tester"};
		
		when(mockClassScanner.findConfigClassesBelow(anyString())).thenReturn(new ArrayList<Class<?>>());
		when(mockLoader.loadConfig(any(Class.class))).thenReturn(new Object());

		PreloadConfigService target = new PreloadConfigService();
		target.initWith(mockLoader, mockClassScanner, scanPackages);
		verify(mockClassScanner, times(2)).findConfigClassesBelow(anyString());
		verifyNoMoreInteractions(mockLoader);
	}


	@Test
	public final void testInit2() {
		ConfigClassScanner mockClassScanner = mock(ConfigClassScanner.class);
		ConfigLoader mockLoader = mock(ConfigLoader.class);	
		String[] scanPackages = {"com.github.hayarobi", "org.eclipse.tester"};
		ArrayList<Class<?>> classes1 = new ArrayList<Class<?>>();
		classes1.add(DataConfig.class);
		classes1.add(ListAndDateConfig.class);
		ArrayList<Class<?>> classes2 = new ArrayList<Class<?>>();
		classes2.add(OtherConfig.class);
		DataConfig dataConfStub = new DataConfig();
		ListAndDateConfig listConfStub = new ListAndDateConfig();
		OtherConfig otherConfStub = new OtherConfig();
		
		when(mockClassScanner.findConfigClassesBelow(anyString())).thenReturn(classes1).thenReturn(classes2);
		when(mockLoader.loadConfig(DataConfig.class)).thenReturn(dataConfStub);
		when(mockLoader.loadConfig(ListAndDateConfig.class)).thenReturn(listConfStub);
		when(mockLoader.loadConfig(OtherConfig.class)).thenReturn(otherConfStub);

		PreloadConfigService target = new PreloadConfigService();
		target.initWith(mockLoader, mockClassScanner, scanPackages);

		assertSame(dataConfStub, target.getConfig(DataConfig.class));
		assertSame(listConfStub, target.getConfig(ListAndDateConfig.class));
		assertSame(otherConfStub, target.getConfig(OtherConfig.class));
		
		try {
			target.getConfig(MapAndAbstractionConfig.class);
			fail();
		}  catch(IllegalArgumentException expectedException) {
			
		}
		verify(mockClassScanner, times(2)).findConfigClassesBelow(anyString());
		verify(mockLoader, times(3)).loadConfig(any(Class.class));
		verifyNoMoreInteractions(mockClassScanner);
		verifyNoMoreInteractions(mockLoader);
	}

}
