package d_pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import c_basePage.BasePage;

/** Editor checks and focused formatting actions. */
public class Ab_DocumentEditorPage extends BasePage {
    @FindBy(css = "#titlebarFileName > span:first-child")
    private WebElement documentTitle;
    @FindBy(id = "showSavedTime")
    private WebElement savedTime;
    @FindBy(id = "save-file")
    private WebElement saveButton;
    @FindBy(id = "btn-reset-file")
    private WebElement resetButton;
    @FindBy(id = "btn-validate-against-schema")
    private WebElement validateButton;
    @FindBy(id = "btn-checkin-file")
    private WebElement checkInButton;

    public Ab_DocumentEditorPage(WebDriver driver) {
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
    private static final By BOLD_TEXT = By.cssSelector("span[data-element='b'][data-style='bold']");
    private static final By EDITOR_FRAMES = By.cssSelector("iframe.cke_wysiwyg_frame");
    // Use the stable command class, not the generated cke_23 ID or decorative icon.
    private static final By BOLD_BUTTON = By.cssSelector("a.cke_button.cke_button__bold");

    private WebElement findVisibleBoldWord() {
        String expected = normalizeEditorText(a_testBase.BaseClass.getConfig("boldSampleText"));
        List<WebElement> matches = new java.util.ArrayList<>();
        for (WebElement element : findElementsNow(BOLD_TEXT)) {
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
        getDriver.switchTo().defaultContent();
        js.executeScript("window.__automationEditorDocument = null;");
        mywait.withMessage("CKEditor must be ready with a stable, editable document")
                .until(driver -> Boolean.TRUE.equals(js.executeScript("""
                        const frames = [...document.querySelectorAll('iframe.cke_wysiwyg_frame')];
                        const library = window.CKEDITOR;
                        let doc = null;
                        if (library) {
                            const editor = Object.values(library.instances).find(e =>
                                e.status === 'ready' && e.document && e.editable()
                                && e.editable().$.isContentEditable
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
                        """)));
    }

    private WebElement locateInEditor(java.util.function.Supplier<WebElement> findElement) {
        waitForEditorReady();
        return mywait.until(driver -> {
            driver.switchTo().defaultContent();
            WebElement word = findElement.get();
            if (word != null) {
                return word;
            }
            for (WebElement frame : findElementsNow(EDITOR_FRAMES)) {
                if (frame.isDisplayed()) {
                    driver.switchTo().frame(frame);
                    word = findElement.get();
                    if (word != null) {
                        return word;
                    }
                    driver.switchTo().defaultContent();
                }
            }
            return null;
        });
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
        try {
            WebElement word = locateBoldWordInEditor();
            if (doubleClick) {
                action.doubleClick(word).perform();
            } else {
                action.click(word).perform();
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
                    word, doubleClick, a_testBase.BaseClass.getConfig("boldSampleText"))));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    /** Checks active formatting, rather than merely whether Bold can be clicked. */
    public boolean isBoldFormattingActive() {
        return waitForBoldFormattingState(true);
    }

    private boolean waitForBoldFormattingState(boolean expectedActive) {
        getDriver.switchTo().defaultContent();
        return mywait.until(driver -> {
            for (WebElement button : findElementsNow(BOLD_BUTTON)) {
                if (!button.isDisplayed()) {
                    continue;
                }
                String classes = " " + getAttributeValue(button, "class").replaceAll("\\s+", " ") + " ";
                boolean enabled = button.isEnabled()
                        && !"true".equalsIgnoreCase(button.getAttribute("aria-disabled"))
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

    private static final By HEADING = By.cssSelector("div[data-element='heading']");
    private static final By UNDO_BUTTON = By.cssSelector("a.cke_button.cke_button__undo");
    private static final String FIND_HEADING_WORD =
            "const root=arguments[0], word=arguments[1];"
            + "const walker=document.createTreeWalker(root,NodeFilter.SHOW_TEXT);"
            + "let node, match=null; while(node=walker.nextNode()){"
            + "const start=node.textContent.indexOf(word); if(start>=0){"
            + "if(match) throw new Error('Heading word is ambiguous');"
            + "match={node:node,start:start};}}"
            + "if(!match) throw new Error('Heading word was not found');"
            + "node=match.node;";

    private WebElement locateHeadingInEditor() {
        return locateInEditor(() -> {
            WebElement match = null;
            for (WebElement heading : findElementsNow(HEADING)) {
                if (heading.isDisplayed() && normalizeEditorText(heading.getText())
                        .equals(normalizeEditorText(a_testBase.BaseClass.getConfig("editorHeadingText")))) {
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
        String expected = normalizeEditorText(a_testBase.BaseClass.getConfig("headingWord"));
        try {
            WebElement heading = locateHeadingInEditor();
            js.executeScript(FIND_HEADING_WORD + """
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
                    """, heading, expected);
            mywait.withMessage("The configured heading word must be selected before applying formatting")
                    .until(driver -> Boolean.TRUE.equals(js.executeScript(
                    "const s=window.getSelection();return !!s && !s.isCollapsed"
                    + " && arguments[0].contains(s.anchorNode) && arguments[0].contains(s.focusNode)"
                    + " && s.toString().replace(/[\\u200B\\u2063\\uFEFF]/g,'').trim()===arguments[1];",
                    heading, expected)));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void requireHeadingBoldIsOff() {
        waitForBoldFormattingState(false);
        if (isHeadingWordExplicitlyBold()) {
            throw new IllegalStateException("Precondition: heading word already has explicit bold markup");
        }
    }

    /** Verify document markup and rendered font weight independently of the toolbar. */
    public boolean isHeadingWordExplicitlyBold() {
        try {
            WebElement heading = locateHeadingInEditor();
            return Boolean.TRUE.equals(js.executeScript(FIND_HEADING_WORD
                    + "const bold=node.parentElement.closest('[data-element=\"b\"][data-style=\"bold\"],b,strong');"
                    + "return !!bold && root.contains(bold)"
                    + " && parseInt(getComputedStyle(node.parentElement).fontWeight,10)>=700;",
                    heading, a_testBase.BaseClass.getConfig("headingWord")));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void applyBoldToSelection() {
        getDriver.switchTo().defaultContent();
        clickElement(BOLD_BUTTON);
    }

    public boolean waitForHeadingWordToBecomeBold() {
        isBoldFormattingActive();
        return mywait.until(driver -> isHeadingWordExplicitlyBold());
    }

    /** Native click on the confirmed Undo command, shared by editing tests. */
    private void clickUndo() {
        getDriver.switchTo().defaultContent();
        WebElement undo = mywait.withMessage("Undo must be visible and enabled before clicking")
                .until(driver -> {
                    for (WebElement button : findElementsNow(UNDO_BUTTON)) {
                        String classes = " " + getAttributeValue(button, "class").replaceAll("\\s+", " ") + " ";
                        // An anchor's isEnabled() alone does not check ARIA-disabled state.
                        // cke_button_off is a normal enabled state for this command.
                        if (button.isDisplayed() && button.isEnabled()
                                && "false".equalsIgnoreCase(button.getAttribute("aria-disabled"))
                                && !classes.contains(" cke_button_disabled ")) {
                            return button;
                        }
                    }
                    return null;
                });
        clickElement(undo);
    }

    /** Undo only this test's formatting command; never save or reset the file. */
    public void undoHeadingBold() {
        clickUndo();
        mywait.withMessage("Undo should remove the heading word's explicit bold formatting")
                .until(driver -> !isHeadingWordExplicitlyBold());
    }

    /** Capture text, rendered weight and explicit formatting for before/after comparison. */
    @SuppressWarnings("unchecked")
    public List<String> getHeadingWordFormattingSnapshot() {
        try {
            WebElement heading = locateHeadingInEditor();
            return (List<String>) js.executeScript(FIND_HEADING_WORD
                    + "const bold=node.parentElement.closest('[data-element=\"b\"][data-style=\"bold\"],b,strong');"
                    + "return [root.textContent.replace(/\\u200b/g,'').trim(),"
                    + "getComputedStyle(node.parentElement).fontWeight,"
                    + "String(!!bold && root.contains(bold))];",
                    heading, a_testBase.BaseClass.getConfig("headingWord"));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    /** Undo the latest Bold edit and reselect its word to check toolbar synchronisation. */
    public void undoHeadingBoldAndWaitForInactiveState() {
        undoHeadingBold();
        selectHeadingWord();
        waitForBoldFormattingState(false);
    }

    private static final By ITALIC_BUTTON = By.cssSelector("a.cke_button.cke_button__italic");
    private static final By UNDERLINE_BUTTON = By.cssSelector("a.cke_button.cke_button__underline");
    private static final By REDO_BUTTON = By.cssSelector("a.cke_button.cke_button__redo");
    private static final By UNORDERED_LIST_BUTTON = By.cssSelector("a.cke_button.cke_button__bulletedlist");
    private static final By ORDERED_LIST_BUTTON = By.cssSelector("a.cke_button.cke_button__numberedlist");
    private static final By PARAGRAPH_OR_LIST_ITEM = By.cssSelector("[data-element='p'],[data-element='li'],p,li");

    private void clickEnabledCommand(By locator) {
        getDriver.switchTo().defaultContent();
        WebElement button = mywait.until(driver -> findElementsNow(locator).stream()
                .filter(element -> element.isDisplayed() && element.isEnabled()
                        && !"true".equalsIgnoreCase(element.getAttribute("aria-disabled"))
                        && !element.getAttribute("class").contains("cke_button_disabled"))
                .findFirst().orElse(null));
        clickElement(button);
    }

    private void waitForCommandActive(By locator) {
        getDriver.switchTo().defaultContent();
        mywait.until(driver -> findElementsNow(locator).stream()
                .anyMatch(button -> button.isDisplayed()
                        && java.util.Arrays.asList(button.getAttribute("class").split("\\s+"))
                                .contains("cke_button_on")));
    }

    private boolean hasHeadingWordStyle(String selector, String property, String expectedValue) {
        try {
            WebElement heading = locateHeadingInEditor();
            return Boolean.TRUE.equals(js.executeScript(FIND_HEADING_WORD
                    + "const format=node.parentElement.closest(arguments[2]);"
                    + "if(!format || !root.contains(format))return false;"
                    + "for(let e=node.parentElement;e && root.contains(e);e=e.parentElement){"
                    + "if(getComputedStyle(e).getPropertyValue(arguments[3]).includes(arguments[4]))return true;}"
                    + "return false;", heading, a_testBase.BaseClass.getConfig("headingWord"),
                    selector, property, expectedValue));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void requireHeadingItalicIsOff() {
        if (isHeadingWordItalic()) {
            throw new IllegalStateException("Precondition: heading word must not already be italic");
        }
    }

    public boolean isHeadingWordItalic() {
        return hasHeadingWordStyle("i,em,[data-element='i'],[data-style='italic']", "font-style", "italic");
    }

    public void applyItalicToSelection() {
        clickEnabledCommand(ITALIC_BUTTON);
    }

    public boolean waitForHeadingWordToBecomeItalic() {
        waitForCommandActive(ITALIC_BUTTON);
        return mywait.until(driver -> isHeadingWordItalic());
    }

    public void undoHeadingItalic() {
        clickUndo();
        mywait.until(driver -> !isHeadingWordItalic());
    }

    public void requireHeadingUnderlineIsOff() {
        if (isHeadingWordUnderlined()) {
            throw new IllegalStateException("Precondition: heading word must not already be underlined");
        }
    }

    public boolean isHeadingWordUnderlined() {
        return hasHeadingWordStyle("u,[data-element='u'],[data-style='underline']",
                "text-decoration-line", "underline");
    }

    public void applyUnderlineToSelection() {
        clickEnabledCommand(UNDERLINE_BUTTON);
    }

    public boolean waitForHeadingWordToBecomeUnderlined() {
        waitForCommandActive(UNDERLINE_BUTTON);
        return mywait.until(driver -> isHeadingWordUnderlined());
    }

    public void undoHeadingUnderline() {
        clickUndo();
        mywait.until(driver -> !isHeadingWordUnderlined());
    }

    public void redoHeadingBold() {
        clickEnabledCommand(REDO_BUTTON);
    }

    private WebElement locateTargetParagraphOrListItem() {
        return locateInEditor(() -> {
            String expected = normalizeEditorText(a_testBase.BaseClass.getConfig("editorParagraphText"));
            return findElementsNow(PARAGRAPH_OR_LIST_ITEM).stream()
                    .filter(element -> element.isDisplayed()
                            && expected.equals(normalizeEditorText(element.getText())))
                    .findFirst().orElse(null);
        });
    }

    private String getTargetParagraphListType() {
        try {
            WebElement paragraph = locateTargetParagraphOrListItem();
            return (String) js.executeScript(
                    "const item=arguments[0].closest('li,[data-element=li]');"
                    + "const list=item?.parentElement.closest('ul,ol,[data-element=ul],[data-element=ol]');"
                    + "return list ? (list.getAttribute('data-element') || list.tagName.toLowerCase()) : '';",
                    paragraph);
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void requireTargetParagraphIsNotAList() {
        if (!getTargetParagraphListType().isEmpty()) {
            throw new IllegalStateException("Precondition: target paragraph must not already be a list item");
        }
    }

    public void applyUnorderedList() {
        clickEnabledCommand(UNORDERED_LIST_BUTTON);
    }

    public void applyOrderedList() {
        clickEnabledCommand(ORDERED_LIST_BUTTON);
    }

    public boolean waitForTargetParagraphInUnorderedList() {
        waitForCommandActive(UNORDERED_LIST_BUTTON);
        return mywait.until(driver -> "ul".equals(getTargetParagraphListType()));
    }

    public boolean waitForTargetParagraphInOrderedList() {
        waitForCommandActive(ORDERED_LIST_BUTTON);
        return mywait.until(driver -> "ol".equals(getTargetParagraphListType()));
    }

    public void undoParagraphList() {
        clickUndo();
        mywait.until(driver -> getTargetParagraphListType().isEmpty());
    }

    private static final By SELECTED_ELEMENT_NAME = By.id("selectedElmName");
    // Confirmed editor paragraph markup; avoid the generated inno_ref attribute.
    private static final By PARAGRAPH = By.cssSelector("div[data-element='p']");

    /** Ignore editor marker characters, preserving meaningful spaces and punctuation. */
    private String normalizeEditorText(String text) {
        return text.replace("\u200B", "").replace("\u2063", "").replace("\uFEFF", "")
                .replaceAll("[\\s\\p{Z}]+", " ").trim();
    }

    private WebElement locateParagraphInEditor() {
        return locateInEditor(() -> {
            WebElement match = null;
            String expected = normalizeEditorText(a_testBase.BaseClass.getConfig("editorParagraphText"));
            for (WebElement paragraph : findElementsNow(PARAGRAPH)) {
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
        try {
            clickPlainBlockWord(locateParagraphInEditor(), a_testBase.BaseClass.getConfig("paragraphSelectionWord"));
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void clickTargetHeading() {
        try {
            clickPlainBlockWord(locateHeadingInEditor(), a_testBase.BaseClass.getConfig("headingSelectionWord"));
        } finally {
            getDriver.switchTo().defaultContent();
        }
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
        action.moveToElement(element, ((Number) point.get(0)).intValue(),
                ((Number) point.get(1)).intValue()).click().perform();
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
        action.moveToElement(element, ((Number) point.get(0)).intValue(),
                ((Number) point.get(1)).intValue()).click().perform();
        mywait.until(driver -> Boolean.TRUE.equals(js.executeScript(
                "const s=window.getSelection();return !!s && s.rangeCount>0 && s.isCollapsed"
                + " && arguments[0].contains(s.anchorNode);", element)));
    }

    public String getSelectedElementType() {
        getDriver.switchTo().defaultContent();
        waitForElementToBeVisible(SELECTED_ELEMENT_NAME);
        return getText(SELECTED_ELEMENT_NAME);
    }

    public void waitForSelectedElementType(String expectedType) {
        getDriver.switchTo().defaultContent();
        mywait.withMessage("Selected Element should change to " + expectedType)
                .until(driver -> expectedType.equals(getSelectedElementType()));
    }

    private String structureHeadingReference;
    private static final By STRUCTURE_HEADINGS = By.cssSelector(
            "#content-scroll li.contentBlockListItem[name='heading']");
    private static final By DIRECT_PARAGRAPH_ROWS = By.cssSelector(
            ":scope > div.contentBlockListItemParent > li.contentBlockListItem[name='p']");

    private static final By DIRECT_STRUCTURE_HEADERS = By.cssSelector(
            ":scope > div.accordion > div.contentBlockAccordian > h2.contentBlockAccordianHeader");

    private WebElement getTargetStructureList() {
        if (structureHeadingReference == null) {
            try {
                structureHeadingReference = getAttributeValue(locateHeadingInEditor(), "inno_ref");
                if (structureHeadingReference.isBlank()) {
                    throw new IllegalStateException("Target heading has no inno_ref for structure scoping");
                }
            } finally {
                getDriver.switchTo().defaultContent();
            }
        }
        getDriver.switchTo().defaultContent();
        return mywait.until(driver -> {
            for (WebElement row : findElementsNow(STRUCTURE_HEADINGS)) {
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
            for (WebElement label : header.findElements(By.cssSelector(".contentBlockAcordianItemName"))) {
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

    /** Set a collapsed caret at the configured text boundary, then send native Enter. */
    public void splitTargetParagraph() {
        try {
            WebElement paragraph = locateParagraphInEditor();
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
                    const index = text.indexOf(boundary);
                    if (index < 0) throw new Error('Split boundary not found in normalised paragraph text');
                    if (text.indexOf(boundary, index + 1) >= 0)
                        throw new Error('Split boundary is ambiguous in paragraph');
                    const position = positions[index];
                    const range = document.createRange();
                    range.setStart(position.node, position.offset);
                    range.collapse(true);
                    const selection = window.getSelection();
                    selection.removeAllRanges();
                    selection.addRange(range);
                    if (!selection.isCollapsed || selection.anchorNode !== position.node
                            || selection.anchorOffset !== position.offset)
                        throw new Error('Caret was not placed at the split boundary');
                    """, paragraph, a_testBase.BaseClass.getConfig("paragraphSplitBefore"));
            action.sendKeys(Keys.ENTER).perform();
        } finally {
            getDriver.switchTo().defaultContent();
        }
    }

    public void undoParagraphSplit(int originalCount) {
        clickUndo();
        waitForParagraphCountInTargetSection(originalCount);
    }
}
