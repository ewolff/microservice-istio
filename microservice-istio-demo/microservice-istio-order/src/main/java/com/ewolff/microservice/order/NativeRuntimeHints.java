package com.ewolff.microservice.order;

import java.util.ArrayList;

import org.postgresql.util.PGobject;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.thymeleaf.engine.IterationStatusVar;

import com.ewolff.microservice.order.customer.Customer;
import com.ewolff.microservice.order.logic.Order;

public class NativeRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		hints.reflection().registerType(OrderFeed.class, MemberCategory.DECLARED_CLASSES,
				MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_METHODS,
				MemberCategory.DECLARED_FIELDS, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
				MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
				MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
				MemberCategory.INVOKE_PUBLIC_METHODS); 
		hints.reflection().registerType(Order.class, MemberCategory.DECLARED_CLASSES,
				MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_METHODS,
				MemberCategory.DECLARED_FIELDS, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
				MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
				MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
				MemberCategory.INVOKE_PUBLIC_METHODS); 
		hints.reflection().registerType(Customer.class, MemberCategory.DECLARED_CLASSES,
				MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS, MemberCategory.INTROSPECT_DECLARED_METHODS,
				MemberCategory.DECLARED_FIELDS, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
				MemberCategory.INTROSPECT_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
				MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
				MemberCategory.INVOKE_PUBLIC_METHODS); 
		hints.reflection().registerType(IterationStatusVar.class, MemberCategory.DECLARED_CLASSES,
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
