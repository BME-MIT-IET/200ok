package org.semanticweb.owlapi6.apitest.swrl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.semanticweb.owlapi6.utilities.OWLAPIStreamUtils.asUnorderedSet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi6.apitest.TestFiles;
import org.semanticweb.owlapi6.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi6.documents.StringDocumentTarget;
import org.semanticweb.owlapi6.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi6.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi6.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi6.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi6.model.AxiomType;
import org.semanticweb.owlapi6.model.OWLDocumentFormat;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.model.SWRLAtom;
import org.semanticweb.owlapi6.model.SWRLClassAtom;
import org.semanticweb.owlapi6.model.SWRLRule;
import org.semanticweb.owlapi6.model.SWRLVariable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date:
 *         04/04/2014
 */
class SWRLAtomOrderingRoundTripTestCase extends TestBase {

    final Set<SWRLAtom> body = new LinkedHashSet<>();
    final Set<SWRLAtom> head = new LinkedHashSet<>();
    SWRLRule rule;

    @BeforeEach
    void setUpPrefixes() {
        SWRLVariable varA = SWRLVariable("http://other.com/A/", "VarA");
        SWRLVariable varB = SWRLVariable("http://other.com/A/", "VarB");
        SWRLVariable varC = SWRLVariable("http://other.com/A/", "VarC");
        SWRLClassAtom atom = SWRLClassAtom(CLASSES.C, varA);
        body.add(atom);
        body.add(SWRLClassAtom(CLASSES.B, varB));
        body.add(SWRLClassAtom(CLASSES.A, varC));
        head.add(SWRLClassAtom(CLASSES.E, varA));
        head.add(SWRLClassAtom(CLASSES.D, varA));
        head.add(atom);
        rule = SWRLRule(body, head);
    }

    @Test
    void individualsShouldNotGetSWRLVariableTypes() {
        OWLOntology o =
            loadFrom(TestFiles.individualSWRLTest, IRIS.iriTest, new RDFXMLDocumentFormat());
        String string = saveOntology(o).toString();
        assertFalse(
            string.contains("<rdf:type rdf:resource=\"http://www.w3.org/2003/11/swrl#Variable\"/>"),
            string);
    }

    @Test
    void shouldPreserveOrderingInRDFXMLRoundTrip() {
        roundTrip(new RDFXMLDocumentFormat());
    }

    private void roundTrip(OWLDocumentFormat ontologyFormat) {
        OWLOntology ont = createAnon();
        ont.add(rule);
        StringDocumentTarget documentTarget = saveOntology(ont, ontologyFormat);
        OWLOntology ont2 = loadFrom(documentTarget, ontologyFormat);
        Set<SWRLRule> rules = asUnorderedSet(ont2.axioms(AxiomType.SWRL_RULE));
        assertEquals(1, rules.size());
        SWRLRule parsedRule = rules.iterator().next();
        assertThat(parsedRule, is(equalTo(rule)));
        List<SWRLAtom> originalBody = new ArrayList<>(body);
        List<SWRLAtom> parsedBody = parsedRule.bodyList();
        assertThat(parsedBody, is(equalTo(originalBody)));
        List<SWRLAtom> originalHead = new ArrayList<>(head);
        List<SWRLAtom> parsedHead = parsedRule.headList();
        assertThat(originalHead, is(equalTo(parsedHead)));
    }

    @Test
    void shouldPreserveOrderingInTurtleRoundTrip() {
        roundTrip(new TurtleDocumentFormat());
    }

    @Test
    void shouldPreserveOrderingInManchesterSyntaxRoundTrip() {
        roundTrip(new ManchesterSyntaxDocumentFormat());
    }

    @Test
    void shouldPreserveOrderingInOWLXMLRoundTrip() {
        roundTrip(new OWLXMLDocumentFormat());
    }
}
