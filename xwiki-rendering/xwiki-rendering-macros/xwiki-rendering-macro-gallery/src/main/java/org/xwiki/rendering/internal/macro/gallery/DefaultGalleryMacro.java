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
package org.xwiki.rendering.internal.macro.gallery;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.Requirement;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.GroupBlock;
import org.xwiki.rendering.internal.macro.MacroContentParser;
import org.xwiki.rendering.macro.AbstractMacro;
import org.xwiki.rendering.macro.MacroExecutionException;
import org.xwiki.rendering.macro.descriptor.DefaultContentDescriptor;
import org.xwiki.rendering.transformation.MacroTransformationContext;
import org.xwiki.skinx.SkinExtension;

/**
 * Displays the images found in the provided content using a slide-show view.
 * 
 * @version $Id$
 * @since 3.0M3
 */
@Component("gallery")
public class DefaultGalleryMacro extends AbstractMacro<Object>
{
    /**
     * The description of the macro.
     */
    private static final String DESCRIPTION =
        "Displays the images found in the provided content using a slide-show view.";

    /**
     * The description of the macro content.
     */
    private static final String CONTENT_DESCRIPTION =
        "The images to be displayed in the gallery. All the images found in the provided wiki content are included. "
            + "Images should be specified using the syntax of the current document. "
            + "Example, for XWiki 2.0 syntax: image:Space.Page@alice.png image:http://www.example.com/path/to/bob.jpg";

    /**
     * The parser used to parse gallery content.
     */
    @Requirement
    private MacroContentParser contentParser;

    /**
     * The component used to import JavaScript file extensions.
     */
    @Requirement("jsfx")
    private SkinExtension jsfx;

    /**
     * The component used to import style-sheet file extensions.
     */
    @Requirement("ssfx")
    private SkinExtension ssfx;

    /**
     * Create and initialize the descriptor of the macro.
     */
    public DefaultGalleryMacro()
    {
        super("Gallery", DESCRIPTION, new DefaultContentDescriptor(CONTENT_DESCRIPTION));
        setDefaultCategory(DEFAULT_CATEGORY_FORMATTING);
    }

    /**
     * {@inheritDoc}
     * 
     * @see AbstractMacro#execute(Object, String, MacroTransformationContext)
     */
    public List<Block> execute(Object parameters, String content, MacroTransformationContext context)
        throws MacroExecutionException
    {
        if (context != null) {
            Map<String, Object> skinExtensionParameters = Collections.singletonMap("forceSkinAction", (Object) true);
            jsfx.use("uicomponents/widgets/gallery/gallery.js", skinExtensionParameters);
            ssfx.use("uicomponents/widgets/gallery/gallery.css", skinExtensionParameters);

            Block galleryBlock = new GroupBlock(Collections.singletonMap("class", "gallery"));
            // Don't execute transformations explicitly. They'll be executed on the generated content later on.
            galleryBlock.addChildren(contentParser.parse(content, context, false, false));
            return Collections.singletonList(galleryBlock);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see AbstractMacro#supportsInlineMode()
     */
    public boolean supportsInlineMode()
    {
        return false;
    }
}
