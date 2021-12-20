package org.semanticweb.owlapi6.obolibrarytest.oboformat;

import static org.semanticweb.owlapi6.apitest.TestFiles.EMPTY_IMPORT;
import static org.semanticweb.owlapi6.apitest.TestFiles.NO_INPUT;
import static org.semanticweb.owlapi6.apitest.TestFiles.UBERON_CORE;
import static org.semanticweb.owlapi6.apitest.TestFiles.UBERON_PATO;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi6.apitest.TestFilenames;
import org.semanticweb.owlapi6.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi6.documents.StringDocumentSource;
import org.semanticweb.owlapi6.formats.OBODocumentFormat;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.model.OWLOntologyCreationException;

class ImportsAndFailuresTestCase extends TestBase {

    protected StringDocumentSource inputSource(String input) {
        return new StringDocumentSource(input, new OBODocumentFormat());
    }

    @Test
    void shouldNotFailOnEmptyImport() throws OWLOntologyCreationException {
        m1.createOntology(IRIS.TEST_IMPORT);
        OWLOntology o = loadFrom(inputSource(EMPTY_IMPORT), m1);
        saveOntology(o, new OBODocumentFormat());
        create(IRIS.TEST_IMPORT);
        OWLOntology o1 = loadFrom(inputSource(EMPTY_IMPORT), m);
        equal(o, o1);
    }

    @Test
    void shouldNotFailOnNoImport() {
        OWLOntology o = loadFrom(inputSource(NO_INPUT));
        roundTrip(o, new OBODocumentFormat());
    }

    @Test
    void shouldNotFailOnPatoImport() {
        load(TestFilenames.PATO_IMPORT_OWL, m1);
        OWLOntology o = loadFrom(inputSource(UBERON_PATO), m1);
        load(TestFilenames.PATO_IMPORT_OWL, m);
        saveOntology(o, new OBODocumentFormat());
        OWLOntology o1 = loadFrom(inputSource(UBERON_PATO), m);
        equal(o, o1);
    }

    @Test
    void shouldNotFailOnNoPatoImport() {
        OWLOntology o = loadFrom(inputSource(UBERON_CORE));
        roundTrip(o, new OBODocumentFormat());
    }
}
