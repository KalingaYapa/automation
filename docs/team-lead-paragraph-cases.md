# Team lead paragraph cases: implementation mapping

Source: the supplied `Pasted text.txt` containing TC-001–TC-013. These external IDs are distinct from existing framework IDs such as TC_01 (document title). Existing IDs and automation methods are preserved.

## Coverage decisions

| Lead ID | Requested case | Framework coverage | Decision |
| --- | --- | --- | --- |
| TC-001 | Enter in the middle | TC_19 — verifyEnterIncreasesParagraphCount | Enhanced the existing case: verify exact text on both sides of the split, section paragraph count, and unchanged other paragraph texts. No duplicate test. |
| TC-002 | Enter at the beginning | TC_30 — verifyEnterAtParagraphBeginning | Added: an empty paragraph precedes the unchanged original. |
| TC-003 | Enter at the end | TC_31 — verifyEnterAtParagraphEnd | Added: an empty paragraph follows the unchanged original. |
| TC-004 | Undo after Enter | TC_19 — verifyEnterIncreasesParagraphCount | Reused and strengthened its existing Undo: verify original paragraph text sequence and count are restored. No duplicate test. |
| TC-005 | Redo after undoing Enter | TC_32 — verifyRedoRestoresParagraphSplit | Added: verify the identical text split returns after Undo/Redo; final Undo restores the document. |
| TC-006 | Paragraph to ordered list | TC_29 — verifyApplyingOrderedListToParagraph | Already automated; reused without adding another case. |
| TC-007 | Paragraph to unordered list | TC_28 — verifyApplyingUnorderedListToParagraph | Already automated; reused without adding another case. |
| TC-008 | Bold on selected paragraph text | TC_33 — verifyApplyingBoldToParagraphWord | Added paragraph-specific coverage and unchanged-surrounding-text checks. Existing TC_15 targets a heading, so it is retained as a different context. |
| TC-009 | Italic on selected paragraph text | TC_34 — verifyApplyingItalicToParagraphWord | Added paragraph-specific coverage and unchanged-surrounding-text checks. Existing TC_25 targets a heading. |
| TC-010 | Underline on selected paragraph text | TC_35 — verifyApplyingUnderlineToParagraphWord | Added paragraph-specific coverage and unchanged-surrounding-text checks. Existing TC_26 targets a heading. |
| TC-011 | Subscript on selected paragraph text | TC_36 — verifyApplyingSubscriptToParagraphWord | Added: smaller font, subscript vertical alignment and unchanged unselected characters. |
| TC-012 | Superscript on selected paragraph text | TC_37 — verifyApplyingSuperscriptToParagraphWord | Added: smaller font, superscript vertical alignment and unchanged unselected characters. |
| TC-013 | Strikethrough on selected paragraph text | TC_38 — verifyApplyingStrikethroughToParagraphWord | Added: line-through decoration and unchanged unselected characters. |

Nine new tests were added; one existing case was enhanced. Twelve application test methods cover the thirteen lead scenarios because the existing split case also verifies Undo. No additional ordered-list or unordered-list tests were created.

## Structure and data

Tests remain in `documenteditor.DocumentEditor1TestCase` (33 cases, below the standard's approximately 50-test class size). Actions remain in `DocumentEditorPageObjects1`; existing navigation, CKEditor selection, frame, toolbar, wait and Undo helpers are reused. Six distinct paragraph formatting scenarios share their preparation/assertion/cleanup steps inside the test class.

Groups are `DocumentEditor` and `P2-DocumentEditor`. The supplied cases do not specify priorities; High was assumed consistently with the existing paragraph-editing cases. Update priority groups if the manual sheet assigns different priorities.

`paragraphFormatWord=flight` is the plain text selected for paragraph formatting. The existing `editorParagraphText` and `paragraphSplitBefore` configure the paragraph and middle split. Before formatting, the test checks that the selected word is not already formatted with the style being tested.

Character snapshots compare the text, font weight, font style, text decoration, font size and vertical alignment. Editor marker characters, tracking IDs and wrapper IDs are excluded; displayed whitespace is normalised. The selected word must gain the requested style, while all unselected character styles and the paragraph text remain unchanged. Undo must restore the original snapshot.

Enter checks capture the current section reference at runtime and compare the entire ordered sequence of its non-list paragraph texts. This avoids hardcoded generated references and catches an incorrectly placed empty paragraph, a split at the wrong position, text loss or unintended changes to neighbouring paragraphs.

All editing scenarios undo their own changes and never save, reset the file or accept changes. INFO and DEBUG step logs remain enabled; credentials are not logged.

## Run the supplied scope

```bash
mvn clean test -DsuiteXmlFile=ParagraphTests.xml
```

Expected: 12 executed methods covering all 13 lead scenarios. The default `mvn clean test` also includes them alongside existing tests, for 38 total application cases. Configure the sandbox login and allocation locally first; see [terminal setup](run-formatting-tests-terminal.md).

## Validation — 25 October 2026

- Initial focused `ParagraphTests.xml` run: **12 passed, 0 failures/errors/skips**.
- Final targeted Maven regression: **17 passed, 0 failures/errors/skips**. This selected the twelve paragraph-scope methods plus existing heading Bold, Undo, Italic, Underline and Redo methods to check the shared selection helper.
- All setup/cleanup methods passed. Exact paragraph text/count restoration and paragraph character-style restoration were verified by the tests.
- Compilation and `python3 scripts/check_framework_standards.py` passed. The checker confirms all **38 application cases** remain enabled, documented, grouped and reachable from the default suites.
- Each selected method emitted one INFO start and successful finish in console output. Credentials were absent from output and temporary generated credentials were removed.
- The complete 38-case default suite was not rerun for this change; validation focused on the new/enhanced/reused scenarios and affected heading-formatting paths.
- SonarQube analysis was not run.
