package documenteditor;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import d_loginpage.BasePage;

/** Editor checks and focused formatting actions. */
public class DocumentEditorPageObjects1 extends BasePage {
    private static final Logger logger = LogManager.getLogger(DocumentEditorPageObjects1.class);

    private static final String ITALIC_STYLE = "italic";
    private static final String UNDERLINE_STYLE = "underline";
    private static final String SPACE = " ";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String WHITESPACE_PATTERN = "\\s+";
    private static final String ARIA_DISABLED_ATTRIBUTE = "aria-disabled";
    private static final String HEADING_WORD_CONFIG = "headingWord";

    @FindBy(xpath = "//span[@data-element='b' and @data-style='bold']")
    private List<WebElement> boldTextElements;
    @FindBy(xpath = "//iframe[contains(concat(' ',normalize-space(@class),' '),' cke_wysiwyg_frame ')]")
    private List<WebElement> editorFrames;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__bold ')]")
    private WebElement boldButton;
    @FindBy(xpath = "//div[@data-element='heading']")
    private List<WebElement> headingElements;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__undo ')]")
    private WebElement undoButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__italic ')]")
    private WebElement italicButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__underline ')]")
    private WebElement underlineButton;
    @FindBy(xpath = "//a[contains(concat(' ',normalize-space(@class),' '),' cke_button__redo ')]")
    private WebElement redoButton;
    @FindBy(xpath = "//*[@data-element='p' or @data-element='li'] | //p | //li")
    private List<WebElement> paragraphsOrListItems;
    @FindBy(xpath = "//div[@data-element='p']")
    private List<WebElement> paragraphElements;

    @FindBy(xpath = "//*[@id='titlebarFileName']/span[1]")
    private WebElement documentTitle;
    @FindBy(xpath = "//*[@id='showSavedTime']")
    private WebElement savedTime;
    @FindBy(xpath = "//*[@id='save-file']")
    private WebElement saveButton;
    @FindBy(xpath = "//*[@id='btn-reset-file']")
    private WebElement resetButton;
    @FindBy(xpath = "//*[@id='btn-validate-against-schema']")
    private WebElement validateButton;
    @FindBy(xpath = "//*[@id='btn-checkin-file']")
    private WebElement checkInButton;

    public DocumentEditorPageObjects1(WebDriver driver) {
        super(driver);
    }

    public boolean isDocumentTitleDisplayed() {
        waitForElementToBeVisible(documentTitle);
        return isElementDisplayed(documentTitle);
    }

    public String getDocumentTitle() {
        waitForElementToBeVisible(documentTitle);
        return getText(documentTitle);
    }

    public boolean isSavedTimeDisplayed() {
        waitForElementToBeVisible(savedTime);
        return isElementDisplayed(savedTime);
    }

    public String getSavedTimeText() {
        waitForElementToBeVisible(savedTime);
        return getText(savedTime);
    }

    public String getSaveButtonText() {
        waitForElementToBeVisible(saveButton);
        return getText(saveButton);
    }

    public boolean isSaveButtonEnabled() {
        waitForElementToBeVisible(saveButton);
        return isElementEnabled(saveButton);
    }

    public String getResetButtonText() {
        waitForElementToBeVisible(resetButton);
        return getText(resetButton);
    }

    public boolean isResetButtonEnabled() {
        waitForElementToBeVisible(resetButton);
        return isElementEnabled(resetButton);
    }

    public String getValidateButtonText() {
        waitForElementToBeVisible(validateButton);
        return getText(validateButton);
    }

    public boolean isValidateButtonEnabled() {
        waitForElementToBeVisible(validateButton);
        return isElementEnabled(validateButton);
    }

    public String getCheckInButtonText() {
        waitForElementToBeVisible(checkInButton);
        return getText(checkInButton);
    }

    public boolean isCheckInButtonEnabled() {
        waitForElementToBeVisible(checkInButton);
        return isElementEnabled(checkInButton);
    }
    // Use the stable command class, not the generated cke_23 ID or decorative icon.

    private WebElement findVisibleBoldWord() {
        String expected = normalizeEditorText(a_testbase.BaseClass.getConfig("boldSampleText"));
        List<WebElement> matches = new java.util.ArrayList<>();
        for (WebElement element : boldTextElements) {
            if (element.isDisplayed() && expected.equals(normalizeEditorText(element.getText()))) {
                matches.add(element);
            }
        }
        if (matches.size() > 1) {
            throw new IllegalStateException("More than one visible bold word matches: " + expected
                    + ". Scope the locator to the intended paragraph.");
        }
        return matches.isEmpty() ? null : matches.get(0);
    }

    /** Supports inline CKEditor and the standard CKEditor 4 editing iframe. */
    private WebElement locateBoldWordInEditor() {
        return locateInEditor(this::findVisibleBoldWord);
    }

    /** Wait beyond the outer header: CKEditor can replace its initial iframe document. */
    public void waitForEditorReady() {
        js.executeScript("window.__automationEditorDocument = null;");
        mywait.withMessage("CKEditor must be ready with a stable, editable document")
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class)
                .until(driver -> {
                    WebElement visibleFrame = editorFrames.stream().filter(WebElement::isDisplayed)
                            .findFirst().orElse(null);
                    return Boolean.TRUE.equals(js.executeScript("""
                        const frames = arguments[0] ? [arguments[0]] : [];
                        const library = window.CKEDITOR;
                        let doc = null;
                        if (library) {
                            const editor = Object.values(library.instances).find(e =>
                                e.status === 'ready' && e.document && e.editable()
                                && e.editable().$.isContentEditable && e.editable().$.isConnected
                                && frames.some(f => f.contentDocument === e.document.$));
                            if (editor) doc = editor.document.$;
                        } else {
                            // Simple inline/iframe editors in the regression fixtures.
                            const docs = frames.length ? frames.map(f => f.contentDocument) : [document];
                            doc = docs.find(d => d?.querySelector('[data-element="heading"]')?.isContentEditable);
                        }
                        // CKEditor readiness is authoritative; rich-text documents may stay interactive.
                        if (!doc?.querySelector('[data-element="heading"]') || doc.fonts.status !== 'loaded') {
                            window.__automationEditorDocument = null;
                            return false;
                        }
                        const stable = window.__automationEditorDocument === doc;
                        window.__automationEditorDocument = doc;
                        return stable;
                        """, visibleFrame));
                });
    }

    /** The caller enters the editor frame before invoking visual-editor operations. */
    private WebElement locateInEditor(java.util.function.Supplier<WebElement> findElement) {
        return mywait.until(driver -> findElement.get());
    }

    /** Places a caret inside the known bold word without modifying its text. */
    public void clickBoldWord() {
        interactWithBoldWord(false);
    }

    /** Selects the known bold word using a native double-click. */
    public void doubleClickBoldWord() {
        interactWithBoldWord(true);
    }

    private void interactWithBoldWord(boolean doubleClick) {
                WebElement word = locateBoldWordInEditor();
        if (doubleClick) {
            logger.debug("Double-clicking existing bold text");
            action.doubleClick(word).perform();
            logger.debug("Double-clicked existing bold text");
        } else {
            logger.debug("Single-clicking existing bold text");
            action.click(word).perform();
            logger.debug("Single-clicked existing bold text");
        }
        // Check native selection in the editing context before inspecting the toolbar.
        // This reads the selection; it does not manufacture one with JavaScript.
        mywait.until(driver -> Boolean.TRUE.equals(js.executeScript(
                "const s = window.getSelection();"
                + "if (!s || !s.rangeCount || !arguments[0].contains(s.anchorNode)"
                + " || !arguments[0].contains(s.focusNode)) return false;"
                + "const clean=t=>t.replace(/[\\u200B\\u2063\\uFEFF]/g,'').replace(/\\s+/g,' ').trim();"
                + "return arguments[1] ? (!s.isCollapsed && clean(s.toString()) === clean(arguments[2]))"
                + " : s.isCollapsed;",
                word, doubleClick, a_testbase.BaseClass.getConfig("boldSampleText"))));

    }

    /** Checks active formatting, rather than merely whether Bold can be clicked. */
    public boolean isBoldFormattingActive() {
        return waitForBoldFormattingState(true);
    }

    private boolean waitForBoldFormattingState(boolean expectedActive) {
        return mywait.until(driver -> {
            for (WebElement button : List.of(boldButton)) {
                if (!button.isDisplayed()) {
                    continue;
                }
                String classes = SPACE + getAttributeValue(button, CLASS_ATTRIBUTE).replaceAll(WHITESPACE_PATTERN, SPACE) + SPACE;
                boolean enabled = button.isEnabled()
                        && !"true".equalsIgnoreCase(button.getAttribute(ARIA_DISABLED_ATTRIBUTE))
                        && !classes.contains(" cke_button_disabled ");
                String expected = expectedActive ? " cke_button_on " : " cke_button_off ";
                String opposite = expectedActive ? " cke_button_off " : " cke_button_on ";
                boolean active = classes.contains(expected) && !classes.contains(opposite);
                if (enabled && active) {
                    return true;
                }
            }
            return false;
        });
    }

    private static final String FIND_TEXT_WORD =
            "const root=arguments[0], word=arguments[1];"
            + "const walker=document.createTreeWalker(root,NodeFilter.SHOW_TEXT);"
            + "let node, match=null; while(node=walker.nextNode()){"
            + "const start=node.textContent.indexOf(word); if(start>=0){"
            + "if(match) throw new Error('Target word is ambiguous');"
            + "match={node:node,start:start};}}"
            + "if(!match) throw new Error('Target word was not found');"
            + "node=match.node;";

    private WebElement locateHeadingInEditor() {
        return locateInEditor(() -> {
            WebElement match = null;
            for (WebElement heading : headingElements) {
                if (heading.isDisplayed() && normalizeEditorText(heading.getText())
                        .equals(normalizeEditorText(a_testbase.BaseClass.getConfig("editorHeadingText")))) {
                    if (match != null) {
                        throw new IllegalStateException("Multiple matching editor headings");
                    }
                    match = heading;
                }
            }
            return match;
        });
    }

    /** Select the exact heading word through the editor's own selection model. */
    public void selectHeadingWord() {
                selectEditorWord(locateHeadingInEditor(),
                normalizeEditorText(a_testbase.BaseClass.getConfig(HEADING_WORD_CONFIG)));

    }

    private void selectEditorWord(WebElement root, String expected) {
        js.executeScript(FIND_TEXT_WORD + """
                const library = parent.CKEDITOR;
                const editor = library && Object.values(library.instances)
                    .find(e => e.status === 'ready' && e.document?.$ === document);
                if (editor) {
                    editor.focus();
                    const range = editor.createRange();
                    const textNode = new library.dom.node(node);
                    range.setStart(textNode, match.start);
                    range.setEnd(textNode, match.start + word.length);
                    editor.getSelection().selectRanges([range]);
                    editor.forceNextSelectionCheck();
                    editor.selectionChange(true);
                } else {
                    arguments[0].closest('[contenteditable=true]')?.focus();
                    const range = document.createRange();
                    range.setStart(node, match.start);
                    range.setEnd(node, match.start + word.length);
                    const selection = window.getSelection();
                    selection.removeAllRanges();
                    selection.addRange(range);
                }
                """, root, expected);
        mywait.withMessage("The configured text word must be selected before applying formatting")
                .until(driver -> Boolean.TRUE.equals(js.executeScript(
                "const s=window.getSelection();return !!s && !s.isCollapsed"
                + " && arguments[0].contains(s.anchorNode) && arguments[0].contains(s.focusNode)"
                + " && s.toString().replace(/[\\u200B\\u2063\\uFEFF]/g,'').trim()===arguments[1];",
                root, expected)));
    }

    public void selectParagraphWord() {
        logger.debug("Selecting the configured paragraph word");
                selectEditorWord(locateParagraphInEditor(),
                normalizeEditorText(a_testbase.BaseClass.getConfig(PARAGRAPH_WORD_CONFIG)));

        logger.debug("Selected the configured paragraph word");
    }


    /** Verify document markup and rendered font weight independently of the toolbar. */
    public boolean isHeadingWordExplicitlyBold() {
                WebElement heading = locateHeadingInEditor();
        return Boolean.TRUE.equals(js.executeScript(FIND_TEXT_WORD
                + "const bold=node.parentElement.closest('[data-element=\"b\"][data-style=\"bold\"],b,strong');"
                + "return !!bold && root.contains(bold)"
                + " && parseInt(getComputedStyle(node.parentElement).fontWeight,10)>=700;",
                heading, a_testbase.BaseClass.getConfig(HEADING_WORD_CONFIG)));

    }

    public void applyBoldToSelection() {
        logger.debug("Clicking Bold toolbar button");
        elementClick(boldButton);
        logger.debug("Clicked Bold toolbar button");
    }

    /** Native click on the confirmed Undo command, shared by editing tests. */
    public void clickUndo() {
        WebElement undo = mywait.withMessage("Undo must be visible and enabled before clicking")
                .until(driver -> {
                    for (WebElement button : List.of(undoButton)) {
                        String classes = SPACE + getAttributeValue(button, CLASS_ATTRIBUTE).replaceAll(WHITESPACE_PATTERN, SPACE) + SPACE;
                        // An anchor's isEnabled() alone does not check ARIA-disabled state.
                        // cke_button_off is a normal enabled state for this command.
                        if (button.isDisplayed() && button.isEnabled()
                                && "false".equalsIgnoreCase(button.getAttribute(ARIA_DISABLED_ATTRIBUTE))
                                && !classes.contains(" cke_button_disabled ")) {
                            return button;
                        }
                    }
                    return null;
                });
        logger.debug("Clicking enabled Undo toolbar button");
        elementClick(undo);
        logger.debug("Clicked Undo toolbar button");
    }

    /** Undo only this test's formatting command; never save or reset the file. */

    /** Capture text, rendered weight and explicit formatting for before/after comparison. */
    @SuppressWarnings("unchecked")
    public List<String> getHeadingWordFormattingSnapshot() {
                WebElement heading = locateHeadingInEditor();
        return (List<String>) js.executeScript(FIND_TEXT_WORD
                + "const bold=node.parentElement.closest('[data-element=\"b\"][data-style=\"bold\"],b,strong');"
                + "return [root.textContent.replace(/\\u200b/g,'').trim(),"
                + "getComputedStyle(node.parentElement).fontWeight,"
                + "String(!!bold && root.contains(bold))];",
                heading, a_testbase.BaseClass.getConfig(HEADING_WORD_CONFIG));

    }

    /** Undo the latest Bold edit and reselect its word to check toolbar synchronisation. */

    private void clickEnabledCommand(WebElement locator) {
        WebElement button = mywait.until(driver -> List.of(locator).stream()
                .filter(element -> element.isDisplayed() && element.isEnabled()
                        && !"true".equalsIgnoreCase(element.getAttribute(ARIA_DISABLED_ATTRIBUTE))
                        && !element.getAttribute(CLASS_ATTRIBUTE).contains("cke_button_disabled"))
                .findFirst().orElse(null));
        logger.debug("Clicking enabled editor toolbar command: {}", locator);
        elementClick(button);
        logger.debug("Clicked editor toolbar command: {}", locator);
    }

    private void waitForCommandActive(WebElement locator) {
        mywait.until(driver -> List.of(locator).stream()
                .anyMatch(button -> button.isDisplayed()
                        && java.util.Arrays.asList(button.getAttribute(CLASS_ATTRIBUTE).split(WHITESPACE_PATTERN))
                                .contains("cke_button_on")));
    }

    private boolean hasHeadingWordStyle(String selector, String property, String expectedValue) {
                WebElement heading = locateHeadingInEditor();
        return Boolean.TRUE.equals(js.executeScript(FIND_TEXT_WORD
                + "const format=node.parentElement.closest(arguments[2]);"
                + "if(!format || !root.contains(format))return false;"
                + "for(let e=node.parentElement;e && root.contains(e);e=e.parentElement){"
                + "if(getComputedStyle(e).getPropertyValue(arguments[3]).includes(arguments[4]))return true;}"
                + "return false;", heading, a_testbase.BaseClass.getConfig(HEADING_WORD_CONFIG),
                selector, property, expectedValue));

    }

    public void requireHeadingItalicIsOff() {
        if (isHeadingWordItalic()) {
            throw new IllegalStateException("Precondition: heading word must not already be italic");
        }
    }

    public boolean isHeadingWordItalic() {
        return hasHeadingWordStyle("i,em,[data-element='i'],[data-style='italic']", "font-style", ITALIC_STYLE);
    }

    public void applyItalicToSelection() {
        clickEnabledCommand(italicButton);
    }

    public void requireHeadingUnderlineIsOff() {
        if (isHeadingWordUnderlined()) {
            throw new IllegalStateException("Precondition: heading word must not already be underlined");
        }
    }

    public boolean isHeadingWordUnderlined() {
        return hasHeadingWordStyle("u,[data-element='u'],[data-style='underline']",
                "text-decoration-line", UNDERLINE_STYLE);
    }

    public void applyUnderlineToSelection() {
        clickEnabledCommand(underlineButton);
    }

    private WebElement locateTargetParagraphOrListItem() {
        return locateInEditor(() -> {
            String expected = normalizeEditorText(a_testbase.BaseClass.getConfig(EDITOR_PARAGRAPH_CONFIG));
            return paragraphsOrListItems.stream()
                    .filter(element -> element.isDisplayed()
                            && expected.equals(normalizeEditorText(element.getText())))
                    .findFirst().orElse(null);
        });
    }

    public String getTargetParagraphListType() {
                WebElement paragraph = locateTargetParagraphOrListItem();
        return (String) js.executeScript(
                "const item=arguments[0].closest('li,[data-element=li]');"
                + "const list=item?.parentElement.closest('ul,ol,[data-element=ul],[data-element=ol]');"
                + "return list ? (list.getAttribute('data-element') || list.tagName.toLowerCase()) : '';",
                paragraph);

    }

    @FindBy(xpath = "//*[@id='selectedElmName']")
    private WebElement selectedElementName;
    // Confirmed editor paragraph markup; avoid the generated inno_ref attribute.

    /** Ignore editor marker characters, preserving meaningful spaces and punctuation. */
    private String normalizeEditorText(String text) {
        return text.replace("\u200B", "").replace("\u2063", "").replace("\uFEFF", "")
                .replaceAll("[\\s\\p{Z}]+", SPACE).trim();
    }

    private WebElement locateParagraphInEditor() {
        return locateInEditor(() -> {
            WebElement match = null;
            String expected = normalizeEditorText(a_testbase.BaseClass.getConfig(EDITOR_PARAGRAPH_CONFIG));
            for (WebElement paragraph : paragraphElements) {
                if (paragraph.isDisplayed() && normalizeEditorText(paragraph.getText()).equals(expected)) {
                    if (match != null) {
                        throw new IllegalStateException("Multiple matching editor paragraphs; scope the locator");
                    }
                    match = paragraph;
                }
            }
            return match;
        });
    }

    public void clickTargetParagraph() {
                clickPlainBlockWord(locateParagraphInEditor(), a_testbase.BaseClass.getConfig("paragraphSelectionWord"));

    }

    public void clickTargetHeading() {
                clickPlainBlockWord(locateHeadingInEditor(), a_testbase.BaseClass.getConfig("headingSelectionWord"));

    }

    /** Click plain text inside the block, avoiding nested formatting and block boundaries. */
    private void clickPlainBlockWord(WebElement element, String word) {
        if (word == null || word.isBlank()) {
            throw new IllegalStateException("Configure a plain selection word for the target block");
        }
        js.executeScript("arguments[0].scrollIntoView({block:'center',inline:'nearest'});", element);
        List<?> point = (List<?>) js.executeScript(
                "const root=arguments[0],word=arguments[1];let hit=null;"
                + "for(const node of root.childNodes){if(node.nodeType!==Node.TEXT_NODE)continue;"
                + "let start=node.textContent.indexOf(word);while(start>=0){"
                + "const before=node.textContent[start-1]||'',after=node.textContent[start+word.length]||'';"
                + "if(!/[\\p{L}\\p{N}_]/u.test(before)&&!/[\\p{L}\\p{N}_]/u.test(after)){"
                + "if(hit)throw new Error('Selection word is ambiguous in plain block text');hit={node,start};}"
                + "start=node.textContent.indexOf(word,start+word.length);}}"
                + "if(!hit)throw new Error('Selection word not found in direct plain block text: '+word);"
                + "const r=document.createRange();r.setStart(hit.node,hit.start);"
                + "r.setEnd(hit.node,hit.start+word.length);const b=r.getClientRects()[0],h=root.getClientRects()[0];"
                + "const cx=Math.floor((Math.max(0,h.left)+Math.min(innerWidth,h.right))/2);"
                + "const cy=Math.floor((Math.max(0,h.top)+Math.min(innerHeight,h.bottom))/2);"
                + "return [Math.floor(b.x+b.width/2)-cx,Math.floor(b.y+b.height/2)-cy];", element, word);
        logger.debug("Clicking measured text position inside the editor block");
        action.moveToElement(element, ((Number) point.get(0)).intValue(),
                ((Number) point.get(1)).intValue()).click().perform();
        logger.debug("Clicked measured editor text position; checking caret");
        mywait.until(driver -> Boolean.TRUE.equals(js.executeScript(
                "const s=window.getSelection();return !!s&&s.rangeCount>0&&s.isCollapsed"
                + "&&s.anchorNode.nodeType===Node.TEXT_NODE"
                + "&&s.anchorNode.parentElement===arguments[0];", element)));
    }

    /** Click actual text, rather than whitespace at the centre of a wide block. */
    private void clickInsideEditorText(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center',inline:'nearest'});", element);
        List<?> point = (List<?>) js.executeScript(
                "const root=arguments[0],walker=document.createTreeWalker(root,NodeFilter.SHOW_TEXT);"
                + "let node,start;while(node=walker.nextNode()){"
                + "start=node.textContent.search(/[^\\s\\u200B\\u2063\\uFEFF]/);if(start>=0)break;}"
                + "if(!node)throw new Error('No visible text to click');"
                + "const r=document.createRange();r.setStart(node,start);r.setEnd(node,start+1);"
                + "const b=r.getClientRects()[0],h=root.getClientRects()[0];"
                + "const cx=Math.floor((Math.max(0,h.left)+Math.min(innerWidth,h.right))/2);"
                + "const cy=Math.floor((Math.max(0,h.top)+Math.min(innerHeight,h.bottom))/2);"
                + "return [Math.floor(b.x+b.width/2)-cx,Math.floor(b.y+b.height/2)-cy];", element);
        logger.debug("Clicking measured text position inside the editor block");
        action.moveToElement(element, ((Number) point.get(0)).intValue(),
                ((Number) point.get(1)).intValue()).click().perform();
        logger.debug("Clicked measured editor text position; checking caret");
        mywait.until(driver -> Boolean.TRUE.equals(js.executeScript(
                "const s=window.getSelection();return !!s && s.rangeCount>0 && s.isCollapsed"
                + " && arguments[0].contains(s.anchorNode);", element)));
    }

    public String getSelectedElementType() {
        waitForElementToBeVisible(selectedElementName);
        return getText(selectedElementName);
    }

    public void waitForSelectedElementType(String expectedType) {
        mywait.withMessage("Selected Element should change to " + expectedType)
                .until(driver -> expectedType.equals(getSelectedElementType()));
    }

    private String structureHeadingReference;
    @FindBy(xpath = "//*[@id='content-scroll']//li[@name='heading']")
    private List<WebElement> structureHeadings;
    private static final By DIRECT_PARAGRAPH_ROWS = By.xpath("./div[contains(concat(' ',normalize-space(@class),' '),' contentBlockListItemParent ')]/li[@name='p']");

    private static final By DIRECT_STRUCTURE_HEADERS = By.xpath("./div[contains(concat(' ',normalize-space(@class),' '),' accordion ')]/div[contains(concat(' ',normalize-space(@class),' '),' contentBlockAccordian ')]/h2");

    private WebElement getTargetStructureList() {
        if (structureHeadingReference == null) {
            throw new IllegalStateException("Capture the section in the editor frame before checking Document Structure");
        }
        return mywait.withMessage("Document Structure must show the selected paragraph's section heading")
                .until(driver -> {
            for (WebElement row : structureHeadings) {
                if (structureHeadingReference.equals(row.getAttribute("id")) && row.isDisplayed()) {
                    return row.findElement(By.xpath(
                            "ancestor::ul[contains(concat(' ',normalize-space(@class),' '), ' contentBlockList ')][1]"));
                }
            }
            return null;
        });
    }

    public int getParagraphCountInTargetSection() {
        WebElement list = getTargetStructureList();
        int count = list.findElements(DIRECT_PARAGRAPH_ROWS).size();
        // Elements with formatting children are rendered as accordion headers, not li rows.
        for (WebElement header : list.findElements(DIRECT_STRUCTURE_HEADERS)) {
            for (WebElement label : header.findElements(By.xpath(".//*[contains(concat(' ',normalize-space(@class),' '),' contentBlockAcordianItemName ')]"))) {
                if ("p".equals(label.getText().trim())) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public void waitForParagraphCountInTargetSection(int expectedCount) {
        mywait.withMessage("Target section should have " + expectedCount + " paragraph rows")
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class)
                .until(driver -> getParagraphCountInTargetSection() == expectedCount);
    }

    /** Place the caret only; the test invokes Enter as a separate visible step. */

    public void placeParagraphCaret(WebElement paragraph, String positionName) {
        captureParagraphEdit(paragraph, positionName);
        clickInsideEditorText(paragraph);
        js.executeScript("""
                const root = arguments[0];
                const markers = /[\\u200B\\u2063\\uFEFF]/;
                const normalise = value => value.replace(/[\\u200B\\u2063\\uFEFF]/g, '')
                    .replace(/\\s+/g, ' ').trim();
                const boundary = normalise(arguments[1]);
                if (!boundary) throw new Error('Split boundary must not be empty');
                const walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT);
                let node, text = '';
                const positions = [];
                // Map each normalised UTF-16 character back to its original DOM position.
                // This allows the boundary to span formatting elements and editor markers.
                while (node = walker.nextNode()) {
                    for (let offset = 0; offset < node.data.length; offset++) {
                        const character = node.data[offset];
                        if (markers.test(character)) continue;
                        if (/\\s/.test(character)) {
                            if (!text || text.endsWith(' ')) continue;
                            text += ' ';
                        } else {
                            text += character;
                        }
                        positions.push({node, offset});
                    }
                }
                let index = text.indexOf(boundary);
                if (arguments[2] === 'beginning') index = 0;
                if (arguments[2] === 'end') index = positions.length - 1;
                if (index < 0) throw new Error('Split boundary not found in normalised paragraph text');
                if (arguments[2] === 'middle' && text.indexOf(boundary, index + 1) >= 0)
                    throw new Error('Split boundary is ambiguous in paragraph');
                const position = positions[index];
                const range = document.createRange();
                const caretOffset = position.offset + (arguments[2] === 'end' ? 1 : 0);
                range.setStart(position.node, caretOffset);
                range.collapse(true);
                const selection = window.getSelection();
                selection.removeAllRanges();
                selection.addRange(range);
                if (!selection.isCollapsed || selection.anchorNode !== position.node
                        || selection.anchorOffset !== caretOffset)
                    throw new Error('Caret was not placed at the split boundary');
                """, paragraph, a_testbase.BaseClass.getConfig(PARAGRAPH_SPLIT_CONFIG), positionName);

    }

    // Column order returned by getParagraphCharacterStyles().
    private static final int CHARACTER_INDEX = 0;
    private static final int FONT_WEIGHT_INDEX = 1;
    private static final int FONT_STYLE_INDEX = 2;
    private static final int TEXT_DECORATION_INDEX = 3;
    private static final int FONT_SIZE_INDEX = 4;
    private static final int VERTICAL_ALIGN_INDEX = 5;
    private static final int BOLD_FONT_WEIGHT = 700;
    private static final String PARAGRAPH_WORD_CONFIG = "paragraphFormatWord";
    private static final String EDITOR_PARAGRAPH_CONFIG = "editorParagraphText";
    private static final String PARAGRAPH_SPLIT_CONFIG = "paragraphSplitBefore";
    private String paragraphSectionReference;
    private List<String> originalParagraphTexts;
    private List<String> expectedParagraphTexts;

    public enum ParagraphFormat {
        BOLD, ITALIC, UNDERLINE, SUBSCRIPT, SUPERSCRIPT, STRIKETHROUGH
    }

    private void captureParagraphEdit(WebElement paragraph, String position) {
        WebElement section = paragraph.findElement(By.xpath("ancestor::*[@data-element='section'][1]"));
        paragraphSectionReference = section.getAttribute("inno_ref");
        if (paragraphSectionReference == null || !paragraphSectionReference.matches("[0-9]+")) {
            throw new IllegalStateException("Paragraph section needs a stable runtime reference");
        }
        originalParagraphTexts = readSectionParagraphTexts(section);
        String text = normalizeEditorText(a_testbase.BaseClass.getConfig(EDITOR_PARAGRAPH_CONFIG));
        int index = originalParagraphTexts.indexOf(text);
        if (index < 0 || originalParagraphTexts.lastIndexOf(text) != index) {
            throw new IllegalStateException("Target paragraph must be unique in its section");
        }
        int split = switch (position) {
            case "beginning" -> 0;
            case "end" -> text.length();
            case "middle" -> text.indexOf(a_testbase.BaseClass.getConfig(PARAGRAPH_SPLIT_CONFIG));
            default -> throw new IllegalArgumentException("Unsupported paragraph caret position: " + position);
        };
        if (split < 0) {
            throw new IllegalStateException("Configured paragraph split boundary is missing");
        }
        java.util.ArrayList<String> expected = new java.util.ArrayList<>(originalParagraphTexts);
        expected.set(index, text.substring(0, split).trim());
        expected.add(index + 1, text.substring(split).trim());
        expectedParagraphTexts = List.copyOf(expected);
    }

    @SuppressWarnings("unchecked")
    private List<String> readSectionParagraphTexts(WebElement section) {
        List<String> texts = (List<String>) js.executeScript("""
                const root=arguments[0];
                return [...root.querySelectorAll('[data-element=p]')]
                    .filter(p=>p.parentElement.closest('[data-element=section]')===root
                        && !p.closest('li,[data-element=li]'))
                    .map(p=>p.textContent.replace(/[\u200B\u2063\uFEFF]/g,'')
                        .replace(/\\s+/g,' ').trim());
                """, section);
        return List.copyOf(texts);
    }

    public List<String> getEditedSectionParagraphTexts() {
                WebElement section = locateInEditor(() -> findElementsNow(By.xpath("//*[@data-element='section' and @inno_ref='" + paragraphSectionReference + "']"))
                .stream().filter(WebElement::isDisplayed).findFirst().orElse(null));
        return readSectionParagraphTexts(section);

    }

    public List<String> getOriginalParagraphTexts() {
        return originalParagraphTexts;
    }

    public List<String> getExpectedParagraphTextsAfterEnter() {
        return expectedParagraphTexts;
    }

    public void waitForParagraphEnterResult() {
        logger.debug("Waiting for the exact paragraph text sequence after Enter");
        mywait.until(driver -> expectedParagraphTexts.equals(getEditedSectionParagraphTexts()));
        logger.debug("Paragraph sequence matches the expected Enter result");
    }

    /** A per-character visual snapshot ignores tracking IDs and invisible editor markers. */
    @SuppressWarnings("unchecked")
    public List<List<String>> getParagraphCharacterStyles() {
        return getParagraphCharacterStyles(locateParagraphInEditor());
    }

    @SuppressWarnings("unchecked")
    public List<List<String>> getParagraphCharacterStyles(WebElement paragraph) {
                return (List<List<String>>) js.executeScript("""
                const root=arguments[0], rows=[];
                const walker=document.createTreeWalker(root,NodeFilter.SHOW_TEXT);
                let node;
                while(node=walker.nextNode()) {
                    const style=getComputedStyle(node.parentElement);
                    const decorations=new Set(); let align=style.verticalAlign;
                    for(let e=node.parentElement;e && root.contains(e);e=e.parentElement) {
                        const css=getComputedStyle(e);
                        for(const part of css.textDecorationLine.split(' '))
                            if(part!=='none')decorations.add(part);
                        if(css.verticalAlign==='sub'||css.verticalAlign==='super')align=css.verticalAlign;
                    }
                    for(const character of node.data) {
                        if(/[\u200B\u2063\uFEFF]/.test(character))continue;
                        const char=/\\s/.test(character)?' ':character;
                        if(char===' ' && (!rows.length||rows.at(-1)[0]===' '))continue;
                        rows.push([char,style.fontWeight,style.fontStyle,
                            [...decorations].sort().join(' '),style.fontSize,align]);
                    }
                }
                while(rows.at(-1)?.[0]===' ')rows.pop();
                return rows;
                """, paragraph);

    }

    private int getParagraphWordStart(List<List<String>> styles) {
        String text = styles.stream().map(row -> row.get(CHARACTER_INDEX)).collect(java.util.stream.Collectors.joining());
        String word = a_testbase.BaseClass.getConfig(PARAGRAPH_WORD_CONFIG);
        int start = text.indexOf(word);
        if (start < 0 || text.indexOf(word, start + word.length()) >= 0) {
            throw new IllegalStateException("Formatting word must occur exactly once in the paragraph");
        }
        return start;
    }

    private boolean isCharacterFormatted(List<String> row, List<String> original, ParagraphFormat format) {
        return switch (format) {
            case BOLD -> Double.parseDouble(row.get(FONT_WEIGHT_INDEX)) >= BOLD_FONT_WEIGHT;
            case ITALIC -> ITALIC_STYLE.equals(row.get(FONT_STYLE_INDEX));
            case UNDERLINE -> row.get(TEXT_DECORATION_INDEX).contains(UNDERLINE_STYLE);
            case STRIKETHROUGH -> row.get(TEXT_DECORATION_INDEX).contains("line-through");
            case SUBSCRIPT, SUPERSCRIPT -> row.get(VERTICAL_ALIGN_INDEX).equals(format == ParagraphFormat.SUBSCRIPT ? "sub" : "super")
                    && Double.parseDouble(row.get(FONT_SIZE_INDEX).replace("px", ""))
                    < Double.parseDouble(original.get(FONT_SIZE_INDEX).replace("px", ""));
        };
    }

    public void requireParagraphWordFormatIsOff(ParagraphFormat format, List<List<String>> original) {
        int start = getParagraphWordStart(original);
        int length = a_testbase.BaseClass.getConfig(PARAGRAPH_WORD_CONFIG).length();
        for (int index = start; index < start + length; index++) {
            if (isCharacterFormatted(original.get(index), original.get(index), format)
                    || (format == ParagraphFormat.SUBSCRIPT && "sub".equals(original.get(index).get(VERTICAL_ALIGN_INDEX)))
                    || (format == ParagraphFormat.SUPERSCRIPT && "super".equals(original.get(index).get(VERTICAL_ALIGN_INDEX)))) {
                throw new IllegalStateException("Paragraph word already has the tested formatting: " + format);
            }
        }
    }

    public boolean isOnlyParagraphWordFormatted(ParagraphFormat format, List<List<String>> original) {
        return isOnlyParagraphWordFormatted(format, original, getParagraphCharacterStyles());
    }

    public boolean isOnlyParagraphWordFormatted(ParagraphFormat format, List<List<String>> original,
            List<List<String>> actual) {
        if (actual.size() != original.size()) {
            return false;
        }
        int start = getParagraphWordStart(original);
        int length = a_testbase.BaseClass.getConfig(PARAGRAPH_WORD_CONFIG).length();
        for (int index = 0; index < original.size(); index++) {
            if (!actual.get(index).get(CHARACTER_INDEX).equals(original.get(index).get(CHARACTER_INDEX))) {
                return false;
            }
            if (index >= start && index < start + length) {
                if (!isCharacterFormatted(actual.get(index), original.get(index), format)) {
                    return false;
                }
            } else if (!actual.get(index).equals(original.get(index))) {
                return false;
            }
        }
        return true;
    }

    public WebElement getEditorFrame() {
        return mywait.until(driver -> editorFrames.stream().filter(WebElement::isDisplayed).findFirst().orElse(null));
    }

    public void rememberTargetSection() {
        structureHeadingReference = locateHeadingInEditor().getAttribute("inno_ref");
    }

    public void clickPlainParagraphWord(WebElement paragraph) {
        clickPlainBlockWord(paragraph, a_testbase.BaseClass.getConfig("paragraphSelectionWord"));
    }

    public void selectParagraphWord(WebElement paragraph) {
        selectEditorWord(paragraph, a_testbase.BaseClass.getConfig(PARAGRAPH_WORD_CONFIG));
    }

    public void clickRedo() {
        clickEnabledCommand(redoButton);
    }

    public boolean isBoldFormattingInactive() {
        return waitForBoldFormattingState(false);
    }

    public boolean isItalicFormattingActive() {
        waitForCommandActive(italicButton);
        return true;
    }

    public boolean isUnderlineFormattingActive() {
        waitForCommandActive(underlineButton);
        return true;
    }

    public void waitForHeadingWordStyle(ParagraphFormat format) {
        mywait.until(driver -> switch (format) {
            case BOLD -> isHeadingWordExplicitlyBold();
            case ITALIC -> isHeadingWordItalic();
            case UNDERLINE -> isHeadingWordUnderlined();
            default -> throw new IllegalArgumentException("Unsupported heading style: " + format);
        });
    }

    public void waitForHeadingFormattingRestored(List<String> original, ParagraphFormat format) {
        mywait.until(driver -> original.equals(getHeadingWordFormattingSnapshot()) && switch (format) {
            case BOLD -> !isHeadingWordExplicitlyBold();
            case ITALIC -> !isHeadingWordItalic();
            case UNDERLINE -> !isHeadingWordUnderlined();
            default -> throw new IllegalArgumentException("Unsupported heading style: " + format);
        });
    }

}
