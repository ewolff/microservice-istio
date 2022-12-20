package com.ewolff.microservice.bonus;

import java.util.ArrayList;

import org.postgresql.util.PGobject;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import com.ewolff.microservice.bonus.poller.OrderFeed;
import com.ewolff.microservice.bonus.poller.OrderFeedEntry;

public class NativeRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		hints.reflection().registerType(OrderFeed.class, MemberCategory.DECLARED_CLASSES,
				MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_METHODS,
				MemberCategory.DECLARED_FIELDS, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
				MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
				MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
				MemberCategory.INVOKE_PUBLIC_METHODS); 
		hints.reflection().registerType(OrderFeedEntry.class, MemberCategory.DECLARED_CLASSES,
				MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_METHODS,
				MemberCategory.DECLARED_FIELDS, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
				MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
				MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
				MemberCategory.INVOKE_PUBLIC_METHODS);
		hints.reflection().registerType(PGobject.class, MemberCategory.DECLARED_CLASSES,
				MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_METHODS,
				MemberCategory.DECLARED_FIELDS, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
				MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
				MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
				MemberCategory.INVOKE_PUBLIC_METHODS);
		hints.reflection().registerType(ArrayList.class, MemberCategory.DECLARED_CLASSES,
				MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_METHODS,
				MemberCategory.DECLARED_FIELDS, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
				MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
				MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
				MemberCategory.INVOKE_PUBLIC_METHODS);
	}

}
