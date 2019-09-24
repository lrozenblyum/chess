package com.leokom.games;

import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;


public class PackageArchitectureTest {
	@Test
	public void commonsShouldNotDependOnChess() {
		// .. denotes 0 or more packages below
		ArchRule packageRule = noClasses()
				.that().resideInAPackage("com.leokom.games.commons..")
				.should().dependOnClassesThat().resideInAPackage("com.leokom.games.chess..");

		JavaClasses javaClasses = new ClassFileImporter().importPackagesOf( GamesPackageMarker.class );
		packageRule.check( javaClasses );
	}
}
