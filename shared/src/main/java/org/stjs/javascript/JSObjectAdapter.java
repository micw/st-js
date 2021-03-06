/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.javascript;

import java.lang.reflect.Method;

import org.stjs.javascript.annotation.Adapter;
import org.stjs.javascript.annotation.Template;

/**
 * here are the methods existent in Javascript for objects and inexistent in the Java counterpart. The generator should
 * generate the correct code
 * 
 * @author acraciun
 */
@Adapter
public final class JSObjectAdapter {
	private JSObjectAdapter() {
		//
	}

	@Template("get")
	public native static Object $get(Object obj, String property);

	@Template("put")
	public native static void $put(Object obj, String property, Object value);

	@Template("adapter")
	public native static boolean hasOwnProperty(Object obj, String property);

	@Template("adapter")
	public static String toLocaleString(Object obj) {
		if (obj == null) {
			throw new Error("ReferenceError", "Cannot access property toLocaleString of null");
		}
		try {
			Method toLocaleString = obj.getClass().getMethod("toLocaleString");
			return (String) toLocaleString.invoke(obj);

		}
		catch (NoSuchMethodException e) {
			// this one could happen. Default behavior of the Object prototy in JS is to call ToString
			// let's do the same
			return JSAbstractOperations.ToString(obj.toString());
		}
		catch (Exception e) {
			// any other error is a real error
			throw new Error("TypeError", "Could not access toLocaleString() method", e);
		}
	}

	@Template("toProperty")
	public native static Map<String, Object> $prototype(Object obj);

	@Template("toProperty")
	public native static Object $constructor(Object obj);

	@Template("properties")
	public native static Map<String, Object> $properties(Object obj);

	@Template("properties")
	public native static <T> T $object(Map<String, Object> properties);

	@Template("js")
	public native static <T> T $js(String code);
}
