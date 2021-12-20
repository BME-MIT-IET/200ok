/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi6.apitest.literals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi6.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi6.model.AddAxiom;
import org.semanticweb.owlapi6.model.OWLLiteral;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.model.OWLOntologyChange;
import org.semanticweb.owlapi6.model.RemoveAxiom;
import org.semanticweb.owlapi6.utility.OWLLiteralReplacer;
import org.semanticweb.owlapi6.utility.OWLObjectTransformer;

/**
 * @author Matthew Horridge, The University of Manchester, Information Management Group
 * @since 3.0.0
 */
class TypedLiteralsTestCase extends TestBase {

    protected OWLOntology createAxioms() {
        return o(DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, LITERALS.LIT_THREE),
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, Literal(33.3)),
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, LITERALS.LIT_TRUE),
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, Literal(33.3f)),
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, Literal("33.3")));
    }

    @Test
    void shouldReplaceLiterals() {
        OWLOntology o = createAxioms();
        OWLLiteralReplacer replacer = new OWLLiteralReplacer(o.getOWLOntologyManager(), set(o));
        Map<OWLLiteral, OWLLiteral> replacements = new HashMap<>();
        replacements.put(LITERALS.LIT_TRUE, LITERALS.LIT_FALSE);
        replacements.put(LITERALS.LIT_THREE, LITERALS.LIT_FOUR);
        List<OWLOntologyChange> results = replacer.changeLiterals(replacements);
        assertResults(o, results);
    }

    protected void assertResults(OWLOntology o, List<OWLOntologyChange> r) {
        assertTrue(r.contains(new AddAxiom(o,
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, LITERALS.LIT_FOUR))));
        assertTrue(r.contains(new AddAxiom(o,
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, LITERALS.LIT_FALSE))));
        assertTrue(r.contains(new RemoveAxiom(o,
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, LITERALS.LIT_THREE))));
        assertTrue(r.contains(new RemoveAxiom(o,
            DataPropertyAssertion(DATAPROPS.DP, INDIVIDUALS.I, LITERALS.LIT_TRUE))));
        assertEquals(4, r.size());
    }

    @Test
    void shouldReplaceLiteralsWithTransformer() {
        OWLOntology o = createAxioms();
        final Map<OWLLiteral, OWLLiteral> replacements = new HashMap<>();
        replacements.put(LITERALS.LIT_TRUE, LITERALS.LIT_FALSE);
        replacements.put(LITERALS.LIT_THREE, LITERALS.LIT_FOUR);
        OWLObjectTransformer<OWLLiteral> replacer = new OWLObjectTransformer<>(in -> true,
            input -> replacements.getOrDefault(input, input), df, OWLLiteral.class);
        List<OWLOntologyChange> results = replacer.change(o);
        assertResults(o, results);
    }
}
