package main.annotations;



/**
 * MARKER ANNOTATION ONLY
 * Markiert, dass ein Attribut durch das Konfigurations-Framework überschrieben werden kann,
 * und das Ändern des gesetzten Wertes deswegen nicht zwingend eine Wirkung zeigen muss.
 * @author Tobias Schaber
 */

@java.lang.annotation.Target(
			value={	java.lang.annotation.ElementType.FIELD,
					java.lang.annotation.ElementType.LOCAL_VARIABLE,
					java.lang.annotation.ElementType.PARAMETER})
public @interface OverriddenByConfiguration {
		
}
