/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.gwt.dom.client;

/**
 * Unit tests for {@link Selection}.
 * 
 * @version $Id$
 */
public class SelectionTest extends AbstractDOMTest
{
    /**
     * This test is mainly for Opera browser which reports a wrong range when the selection ends before an image.
     */
    public void testEndSelectionBeforeImage()
    {
        getContainer().setInnerHTML("ab<em>cd</em>ef<ins>gh<img/></ins>ij");

        Range range = getDocument().createRange();
        range.setStart(getContainer().getChildNodes().getItem(1).getFirstChild(), 1);
        range.setEndAfter(getContainer().getChildNodes().getItem(3).getFirstChild());

        Selection selection = getDocument().getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
        assertEquals("defgh", selection.toString());

        range = selection.getRangeAt(0);
        assertTrue(DOMUtils.getInstance().comparePoints(range.getEndContainer(), range.getEndOffset(),
            getContainer().getChildNodes().getItem(3), 1) <= 0);
    }
}
