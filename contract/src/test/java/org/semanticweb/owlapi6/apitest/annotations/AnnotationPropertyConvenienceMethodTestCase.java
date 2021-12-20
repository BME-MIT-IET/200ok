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
package org.semanticweb.owlapi6.apitest.annotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.semanticweb.owlapi6.model.parameters.Imports.EXCLUDED;
import static org.semanticweb.owlapi6.model.parameters.Imports.INCLUDED;
import static org.semanticweb.owlapi6.search.Filters.subAnnotationWithSuper;
import static org.semanticweb.owlapi6.search.Searcher.sub;
import static org.semanticweb.owlapi6.search.Searcher.sup;
import static org.semanticweb.owlapi6.utilities.OWLAPIStreamUtils.asUnorderedSet;
import static org.semanticweb.owlapi6.utilities.OWLAPIStreamUtils.contains;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi6.apitest.baseclasses.TestBase;
import org.semanticweb.owlapi6.model.OWLAxiom;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.search.Filters;

/**
 * @author Matthew Horridge, The University of Manchester, Bio-Health Informatics Group
 * @since 3.2.0
 */
class AnnotationPropertyConvenienceMethodTestCase extends TestBase {

    @Test
    void testGetSuperProperties() {
        OWLOntology ont = create("OntA");
        ont.addAxioms(SubAnnotationPropertyOf(ANNPROPS.propP, ANNPROPS.propQ),
            SubAnnotationPropertyOf(ANNPROPS.propP, ANNPROPS.propR));
        Collection<OWLAxiom> axioms =
            asUnorderedSet(ont.axioms(Filters.subAnnotationWithSub, ANNPROPS.propP, INCLUDED));
        assertTrue(contains(sup(axioms.stream()), ANNPROPS.propQ));
        assertTrue(contains(sup(axioms.stream()), ANNPROPS.propR));
        axioms = asUnorderedSet(ont.axioms(Filters.subAnnotationWithSub, ANNPROPS.propP, EXCLUDED));
        assertTrue(contains(sup(axioms.stream()), ANNPROPS.propQ));
        assertTrue(contains(sup(axioms.stream()), ANNPROPS.propR));
    }

    @Test
    void testGetSubProperties() {
        OWLOntology ont = create("OntA");
        ont.addAxioms(SubAnnotationPropertyOf(ANNPROPS.propP, ANNPROPS.propQ),
            SubAnnotationPropertyOf(ANNPROPS.propP, ANNPROPS.propR));
        assertTrue(contains(sub(ont.axioms(subAnnotationWithSuper, ANNPROPS.propQ, INCLUDED)),
            ANNPROPS.propP));
        assertTrue(contains(sub(ont.axioms(subAnnotationWithSuper, ANNPROPS.propQ, EXCLUDED)),
            ANNPROPS.propP));
        assertTrue(contains(sub(ont.axioms(subAnnotationWithSuper, ANNPROPS.propR, INCLUDED)),
            ANNPROPS.propP));
        assertTrue(contains(sub(ont.axioms(subAnnotationWithSuper, ANNPROPS.propR, EXCLUDED)),
            ANNPROPS.propP));
    }
}
