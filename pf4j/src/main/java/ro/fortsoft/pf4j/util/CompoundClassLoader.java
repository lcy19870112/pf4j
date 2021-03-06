/*
 * Copyright 2012 Decebal Suiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.pf4j.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class loader that has multiple loaders and uses them for loading classes and resources.
 *
 * @author Decebal Suiu
 */
public class CompoundClassLoader extends ClassLoader {

	private Set<ClassLoader> loaders = new HashSet<>();

	public void addLoader(ClassLoader loader) {
		loaders.add(loader);
	}

	public void removeLoader(ClassLoader loader) {
		loaders.remove(loader);
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		for (ClassLoader loader : loaders) {
			try {
				return loader.loadClass(name);
			} catch (ClassNotFoundException e) {
				// try next
			}
		}

		throw new ClassNotFoundException(name);
	}

	@Override
	public URL findResource(String name) {
		for (ClassLoader loader : loaders) {
			URL url = loader.getResource(name);
			if (url != null) {
				return url;
			}
		}

		return null;
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		List<URL> resources = new ArrayList<>();
		for (ClassLoader loader : loaders) {
			resources.addAll(Collections.list(loader.getResources(name)));
		}

		return Collections.enumeration(resources);
	}

}
