package org.semanticweb.owlapi6.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi6.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi6.model.IRI;
import org.semanticweb.owlapi6.model.OWLEntity;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.model.OWLOntologyManager;
import org.semanticweb.owlapi6.utilities.OWLAPIStreamUtils;
import org.semanticweb.owlapi6.utility.OWLEntityURIConverter;

public class OWLEntityURIConverterTest extends TestBase {

    private static final String TEST_ONTOLOGY_RESOURCE = "testUriConverterOntology.owl";
    private static final String OLD_NAMESPACE = "http://www.example.org/testOntology#";
    private static final String NEW_NAMESPACE = "http://www.example.org/newTestOntology#";

    @Test
    public void test() {
        OWLOntology ontology = loadOntology(TEST_ONTOLOGY_RESOURCE);
        entities(ontology).forEach(OWLEntityURIConverterTest::assertOldName);
        OWLEntityURIConverter converter =
            getOWLEntityNamespaceConverter(ontology.getOWLOntologyManager());
        ontology.applyChanges(converter.getChanges());
        entities(ontology).forEach(OWLEntityURIConverterTest::assertCorrectRename);
    }

    protected Stream<OWLEntity> entities(OWLOntology ontology) {
        return ontology.signature().filter(x -> !x.getIRI().isReservedVocabulary());
    }

    private static OWLEntityURIConverter getOWLEntityNamespaceConverter(
        OWLOntologyManager manager) {
        return new OWLEntityURIConverter(manager, OWLAPIStreamUtils.asList(manager.ontologies()),
            OWLEntityURIConverterTest::rename);
    }

    protected static IRI rename(OWLEntity entity) {
        String iriString = entity.getIRI().getIRIString();
        if (iriString.contains(OLD_NAMESPACE)) {
            return df.getIRI(iriString.replace(OLD_NAMESPACE, NEW_NAMESPACE));
        }
        return entity.getIRI();
    }

    protected static void assertCorrectRename(OWLEntity x) {
        assertTrue(x.getIRI().getIRIString().contains(NEW_NAMESPACE), x.getIRI().toString());
    }

    protected static void assertOldName(OWLEntity x) {
        assertTrue(x.getIRI().getIRIString().contains(OLD_NAMESPACE), x.getIRI().toString());
    }
}
