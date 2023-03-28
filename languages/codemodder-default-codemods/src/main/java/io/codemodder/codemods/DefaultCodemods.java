package io.codemodder.codemods;

import io.codemodder.Changer;
import java.util.List;

/**
 * Give an ability for users to list all the codemods so they don't have to reference them
 * individually.
 */
public final class DefaultCodemods {

  /** Get a list of all the codemods in our default set. */
  public static List<Class<? extends Changer>> asList() {
    return List.of(
        HardenJavaDeserializationCodemod.class,
        HardenXMLDecoderCodemod.class,
        LimitReadlineCodemod.class,
        RandomizeSeedCodemod.class,
        SanitizeMultipartFilenameCodemod.class,
        SecureRandomCodemod.class,
        SSRFCodemod.class,
        VerbTamperingCodemod.class);
  }
}
