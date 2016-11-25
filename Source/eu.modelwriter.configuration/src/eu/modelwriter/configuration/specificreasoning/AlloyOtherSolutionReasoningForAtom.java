package eu.modelwriter.configuration.specificreasoning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import eu.modelwriter.configuration.alloy.RUN_TYPE;
import eu.modelwriter.configuration.alloy.reasoning.AlloyParserForReasoning;
import eu.modelwriter.configuration.alloy.reasoning.AlloyValidatorReasoning;
import eu.modelwriter.configuration.alloy.reasoning.InstanceTranslatorReasoning;
import eu.modelwriter.configuration.internal.AlloyUtilities;
import eu.modelwriter.marker.internal.MarkerFactory;
import eu.modelwriter.traceability.core.persistence.AtomType;
import eu.modelwriter.traceability.core.persistence.DocumentRoot;
import eu.modelwriter.traceability.core.persistence.FieldType;
import eu.modelwriter.traceability.core.persistence.TupleType;
import eu.modelwriter.traceability.core.persistence.persistenceFactory;
import eu.modelwriter.traceability.core.persistence.internal.ModelIO;

public class AlloyOtherSolutionReasoningForAtom {

  private static AlloyOtherSolutionReasoningForAtom instance;
  private static String baseFileDirectory = ResourcesPlugin.getWorkspace().getRoot().getLocation()
      + " .modelwriter reasoningForAtom ".replace(" ", System.getProperty("file.separator"));
  private static final String alsPath =
      AlloyOtherSolutionReasoningForAtom.baseFileDirectory + "reasoningForAtom.als";
  private static final String xmlPath =
      AlloyOtherSolutionReasoningForAtom.baseFileDirectory + "reasoningForAtom.xml";
  private static String atomName;
  private static Map<String, List<String>> reasonRelations;
  private static List<A4Solution> solutions;
  private static List<Map<Integer, List<TupleType>>> reasonedTuples;
  private static int currentSolutionIndex;
  private static int MAX_NEXT_SOLUTION_ATTEMPT = 25;
  private static int CURRENT_NEXT_SOLUTION_ATTEMPT;

  public static AlloyOtherSolutionReasoningForAtom getInstance() {
    if (AlloyOtherSolutionReasoningForAtom.instance == null) {
      AlloyOtherSolutionReasoningForAtom.instance = new AlloyOtherSolutionReasoningForAtom();
      AlloyOtherSolutionReasoningForAtom.reasonRelations = new HashMap<>();
      AlloyOtherSolutionReasoningForAtom.solutions = new ArrayList<>();
      AlloyOtherSolutionReasoningForAtom.reasonedTuples = new ArrayList<>();
    }

    return AlloyOtherSolutionReasoningForAtom.instance;
  }

  public void setAtomName(final String atomName) {
    AlloyOtherSolutionReasoningForAtom.atomName = atomName;
  }

  public boolean start(final String atomName, final String atomType) throws Err {
    AlloyOtherSolutionReasoningForAtom.atomName = atomName;
    final File reasoningXml = new File(AlloyOtherSolutionReasoningForAtom.xmlPath);
    if (reasoningXml.exists()) {
      reasoningXml.delete();
    }
    final File reasoningAls = new File(AlloyOtherSolutionReasoningForAtom.alsPath);
    if (reasoningAls.exists()) {
      reasoningAls.delete();
    }

    final InstanceTranslatorReasoning instanceTranslator =
        new InstanceTranslatorReasoning(AlloyOtherSolutionReasoningForAtom.baseFileDirectory,
            AlloyOtherSolutionReasoningForAtom.alsPath);
    instanceTranslator.translate();
    AlloyOtherSolutionReasoningForAtom.reasonRelations = instanceTranslator.getReasonRelations();

    if (!AlloyValidatorReasoning.validate(AlloyOtherSolutionReasoningForAtom.alsPath)) {
      return false;
    }

    final AlloyParserForReasoning parser =
        new AlloyParserForReasoning(AlloyOtherSolutionReasoningForAtom.alsPath);
    final A4Solution solution = parser.parse();
    if (solution.satisfiable()) {
      solution.writeXML(AlloyOtherSolutionReasoningForAtom.xmlPath);
      parse(AlloyOtherSolutionReasoningForAtom.xmlPath);
      AlloyOtherSolutionReasoningForAtom.solutions.add(solution);
      AlloyOtherSolutionReasoningForAtom.currentSolutionIndex = 0;
      AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT = 0;
    }

    return reasoning(RUN_TYPE.START);
  }

  public boolean next() throws Err {
    A4Solution solution;
    if (AlloyOtherSolutionReasoningForAtom.solutions
        .size() == AlloyOtherSolutionReasoningForAtom.currentSolutionIndex + 1) {
      solution = AlloyOtherSolutionReasoningForAtom.solutions
          .get(AlloyOtherSolutionReasoningForAtom.currentSolutionIndex).next();
      if (solution.equals(AlloyOtherSolutionReasoningForAtom.solutions
          .get(AlloyOtherSolutionReasoningForAtom.currentSolutionIndex))) {
        return false;
      } else if (solution.satisfiable()) {
        AlloyOtherSolutionReasoningForAtom.solutions.add(solution);
        AlloyOtherSolutionReasoningForAtom.currentSolutionIndex++;
      } else {
        return false;
      }
    } else {
      solution = AlloyOtherSolutionReasoningForAtom.solutions
          .get(AlloyOtherSolutionReasoningForAtom.currentSolutionIndex + 1);
      AlloyOtherSolutionReasoningForAtom.currentSolutionIndex++;
    }

    solution.writeXML(AlloyOtherSolutionReasoningForAtom.xmlPath);
    parse(AlloyOtherSolutionReasoningForAtom.xmlPath);

    return reasoning(RUN_TYPE.NEXT);
  }

  public boolean previous() throws Err {
    A4Solution solution;
    if (AlloyOtherSolutionReasoningForAtom.currentSolutionIndex == 0) {
      return false;
    } else {
      solution = AlloyOtherSolutionReasoningForAtom.solutions
          .get(AlloyOtherSolutionReasoningForAtom.currentSolutionIndex - 1);
      AlloyOtherSolutionReasoningForAtom.currentSolutionIndex--;
    }

    solution.writeXML(AlloyOtherSolutionReasoningForAtom.xmlPath);
    parse(AlloyOtherSolutionReasoningForAtom.xmlPath);

    return reasoning(RUN_TYPE.PREVIOUS);
  }

  private void parse(final String xmlPath) {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
      final File file = new File(xmlPath);
      final Document document = builder.parse(file);
      final Node instance = document.getElementsByTagName("instance").item(0);
      instance.getAttributes().removeNamedItem("command");

      Transformer transformer;
      try {
        transformer = TransformerFactory.newInstance().newTransformer();
        final DOMSource source = new DOMSource(document);
        final StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      } catch (final Exception e) {
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  private void removeOldReasoning(final RUN_TYPE runType) {
    final DocumentRoot documentRoot = AlloyUtilities.getDocumentRoot();
    int solutionNumber =
        runType == RUN_TYPE.NEXT ? AlloyOtherSolutionReasoningForAtom.currentSolutionIndex - 1
            : runType == RUN_TYPE.PREVIOUS
            ? AlloyOtherSolutionReasoningForAtom.currentSolutionIndex + 1 : -1;
    solutionNumber = solutionNumber >= AlloyOtherSolutionReasoningForAtom.reasonedTuples.size() ? -1
        : solutionNumber;
    if (solutionNumber == -1) {
      return;
    }

    final Iterator<Entry<Integer, List<TupleType>>> oldReasonedTuplesIterator =
        AlloyOtherSolutionReasoningForAtom.reasonedTuples.get(solutionNumber).entrySet().iterator();
    final EList<FieldType> fieldTypes = documentRoot.getAlloy().getInstance().getField();
    while (oldReasonedTuplesIterator.hasNext()) {
      final Entry<Integer, List<TupleType>> entry = oldReasonedTuplesIterator.next();
      for (final FieldType fieldType : fieldTypes) {
        if (fieldType.getID() == entry.getKey()) {
          for (final TupleType oldTupleType : entry.getValue()) {
            final Iterator<TupleType> tupleIter = fieldType.getTuple().iterator();
            final AtomType oldAtomType0 = oldTupleType.getAtom().get(0);
            final AtomType oldAtomType1 = oldTupleType.getAtom().get(1);
            while (tupleIter.hasNext()) {
              final TupleType tupleType = tupleIter.next();
              if (tupleType.isReasoned()) {
                final AtomType atomType0 = tupleType.getAtom().get(0);
                final AtomType atomType1 = tupleType.getAtom().get(1);
                if (oldAtomType0.getLabel().equals(atomType0.getLabel())
                    && oldAtomType1.getLabel().equals(atomType1.getLabel())) {
                  tupleIter.remove();
                }
              }
            }
          }
        }
      }
    }

    final int reasonedTupleCount = calcReasonedTupleCount(
        AlloyOtherSolutionReasoningForAtom.reasonedTuples.get(solutionNumber));
    MarkerFactory.rewindId(reasonedTupleCount, documentRoot);

    AlloyUtilities.writeDocumentRoot(documentRoot);
  }

  public void finish() {
    AlloyOtherSolutionReasoningForAtom.solutions.clear();
    AlloyOtherSolutionReasoningForAtom.currentSolutionIndex = 0;
    AlloyOtherSolutionReasoningForAtom.reasonRelations.clear();
    AlloyOtherSolutionReasoningForAtom.reasonedTuples.clear();
  }

  public DocumentRoot getDocumentRoot() {
    @SuppressWarnings("rawtypes")
    final ModelIO modelIO = new ModelIO<>();
    @SuppressWarnings("rawtypes")
    List list = null;
    try {
      list = modelIO.read(URI.createFileURI(AlloyOtherSolutionReasoningForAtom.xmlPath));
    } catch (final IOException e) {
      return null;
    }
    if (list == null || list.isEmpty()) {
      return null;
    }
    final DocumentRoot documentRoot = (DocumentRoot) list.get(0);
    return documentRoot;
  }

  public A4Solution getCurrentSolution() {
    return AlloyOtherSolutionReasoningForAtom.solutions.size() == 0 ? null
        : AlloyOtherSolutionReasoningForAtom.solutions
        .get(AlloyOtherSolutionReasoningForAtom.currentSolutionIndex);
  }

  private boolean reasoning(final RUN_TYPE runType) throws Err {
    removeOldReasoning(runType);

    final DocumentRoot documentRootReasoning = getDocumentRoot();
    final DocumentRoot documentRootOriginal = AlloyUtilities.getDocumentRoot();

    int reasonedTupleCount = 0;
    if (AlloyOtherSolutionReasoningForAtom.solutions
        .size() > AlloyOtherSolutionReasoningForAtom.reasonedTuples.size()) {
      final Map<Integer, List<TupleType>> newReasonedTuples = findReasonedTuples(
          documentRootOriginal, documentRootReasoning, AlloyOtherSolutionReasoningForAtom.atomName);
      reasonedTupleCount = calcReasonedTupleCount(newReasonedTuples);
      AlloyOtherSolutionReasoningForAtom.reasonedTuples.add(newReasonedTuples);
      if (reasonedTupleCount == 0 || isRequiredPass(reasonedTupleCount, newReasonedTuples)) {
        if (AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT > AlloyOtherSolutionReasoningForAtom.MAX_NEXT_SOLUTION_ATTEMPT) {
          final boolean rollBackSucceed = rollBackNextAttemption(false);
          if (rollBackSucceed) {
            addNewReasoning(AlloyOtherSolutionReasoningForAtom.reasonedTuples
                .get(AlloyOtherSolutionReasoningForAtom.currentSolutionIndex));
          }
          return false;
        }
        AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT++;
        return next();
      } else {
        if (AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT > 0) {
          rollBackNextAttemption(true);
        }
        addNewReasoning(newReasonedTuples);
      }
    } else {
      final Map<Integer, List<TupleType>> existingReasonedTuples =
          AlloyOtherSolutionReasoningForAtom.reasonedTuples
          .get(AlloyOtherSolutionReasoningForAtom.currentSolutionIndex);
      reasonedTupleCount = calcReasonedTupleCount(existingReasonedTuples);
      addNewReasoning(existingReasonedTuples);
    }

    JOptionPane.showMessageDialog(null,
        "Reasoning on relations successfully completed.\nReasoned relation count: "
            + reasonedTupleCount,
            "Reasoning on Relations of Selected Atom", JOptionPane.WARNING_MESSAGE);
    return true;
  }

  private boolean rollBackNextAttemption(final boolean lastSolutionIsValid) {
    final int lastSolutionIndex = AlloyOtherSolutionReasoningForAtom.solutions.size() - 1;
    int validSolutionIndex = lastSolutionIsValid ? lastSolutionIndex
        : lastSolutionIndex - AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT - 1;
    AlloyOtherSolutionReasoningForAtom.currentSolutionIndex = lastSolutionIsValid
        ? lastSolutionIndex - AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT
            : lastSolutionIndex - AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT - 1;
    AlloyOtherSolutionReasoningForAtom.CURRENT_NEXT_SOLUTION_ATTEMPT = 0;

    validSolutionIndex = validSolutionIndex > 0 ? validSolutionIndex : 0;
    AlloyOtherSolutionReasoningForAtom.currentSolutionIndex =
        AlloyOtherSolutionReasoningForAtom.currentSolutionIndex > 0
        ? AlloyOtherSolutionReasoningForAtom.currentSolutionIndex : 0;

        if (AlloyOtherSolutionReasoningForAtom.reasonedTuples.get(validSolutionIndex).size() == 0) {
          finish();
          return false;
        }

        AlloyOtherSolutionReasoningForAtom.solutions.set(
            AlloyOtherSolutionReasoningForAtom.currentSolutionIndex,
            AlloyOtherSolutionReasoningForAtom.solutions.get(validSolutionIndex));
        AlloyOtherSolutionReasoningForAtom.reasonedTuples.set(
            AlloyOtherSolutionReasoningForAtom.currentSolutionIndex,
            AlloyOtherSolutionReasoningForAtom.reasonedTuples.get(validSolutionIndex));

        for (int i =
            lastSolutionIndex; i > AlloyOtherSolutionReasoningForAtom.currentSolutionIndex; i--) {
          AlloyOtherSolutionReasoningForAtom.solutions.remove(i);
          AlloyOtherSolutionReasoningForAtom.reasonedTuples.remove(i);
        }
        return true;
  }

  private void addNewReasoning(final Map<Integer, List<TupleType>> newReasonedTuples) {
    final DocumentRoot documentRoot = AlloyUtilities.getDocumentRoot();

    final Iterator<FieldType> fieldIterator =
        documentRoot.getAlloy().getInstance().getField().iterator();
    while (fieldIterator.hasNext()) {
      final FieldType fieldType = fieldIterator.next();
      final List<TupleType> tuples = newReasonedTuples.get(fieldType.getID());
      if (tuples != null) {
        fieldType.getTuple().addAll(tuples);
      }
    }

    AlloyUtilities.writeDocumentRoot(documentRoot);
  }

  private int calcReasonedTupleCount(final Map<Integer, List<TupleType>> newReasonedTuples) {
    int reasonedTupleCount = 0;
    for (final Entry<Integer, List<TupleType>> entry : newReasonedTuples.entrySet()) {
      reasonedTupleCount += entry.getValue().size();
    }
    return reasonedTupleCount;
  }

  private Map<Integer, List<TupleType>> findReasonedTuples(final DocumentRoot documentRootOriginal,
      final DocumentRoot documentRootReasoning, final String atomName) throws Err {
    final Map<Integer, List<TupleType>> newReasonedTuples = new HashMap<>();

    for (final FieldType fieldType_R : documentRootReasoning.getAlloy().getInstance().getField()) {
      for (final FieldType fieldType_O : documentRootOriginal.getAlloy().getInstance().getField()) {
        if (!fieldType_R.getLabel().equals(fieldType_O.getLabel())) {
          continue;
        }

        final int sourceId_R = fieldType_R.getParentID();
        final String sourceSigName_R =
            AlloyUtilities.getSigNameById(sourceId_R, documentRootReasoning);
        if (!AlloyOtherSolutionReasoningForAtom.reasonRelations.containsKey(sourceSigName_R)
            || !AlloyOtherSolutionReasoningForAtom.reasonRelations.get(sourceSigName_R)
            .contains(fieldType_R.getLabel())) {
          continue;
        }

        final int sourceId_O = fieldType_O.getParentID();
        final String sourceSigName_O =
            AlloyUtilities.getSigNameById(sourceId_O, documentRootOriginal);
        if (!AlloyOtherSolutionReasoningForAtom.reasonRelations.containsKey(sourceSigName_O)
            || !AlloyOtherSolutionReasoningForAtom.reasonRelations.get(sourceSigName_O)
            .contains(fieldType_O.getLabel())) {
          continue;
        }

        if (!sourceSigName_O.equals(sourceSigName_R)) {
          continue;
        }

        if (fieldType_O.getTuple().size() == fieldType_R.getTuple().size()) {
          continue;
        }

        for (final TupleType tuple_R : fieldType_R.getTuple()) {
          AtomType atomType0_R = getOriginalAtomType(tuple_R.getAtom().get(0).getLabel());
          final AtomType atomType1_R = getOriginalAtomType(tuple_R.getAtom().get(1).getLabel());

          if (atomType0_R == null || atomType1_R == null) {
            continue;
          }

          boolean exists = false;
          for (final TupleType tuple_O : fieldType_O.getTuple()) {
            if (atomType0_R.getLabel().equals(tuple_O.getAtom().get(0).getLabel())
                && atomType1_R.getLabel().equals(tuple_O.getAtom().get(1).getLabel())) {
              exists = true;
              break;
            }
          }

          if (!exists || fieldType_O.getTuple().size() == 0) {
            final TupleType tupleType = persistenceFactory.eINSTANCE.createTupleType();
            if (atomType0_R.equals(atomType1_R)) {
              atomType0_R = AlloyUtilities.cloneAtomType(atomType0_R);
            }
            tupleType.getAtom().add(atomType0_R);
            tupleType.getAtom().add(atomType1_R);
            tupleType.setReasoned(true);

            if (!AlloyUtilities.getAtomNameById(atomType0_R.getLabel()).replace("$", "")
                .equals(atomName)
                && !AlloyUtilities.getAtomNameById(atomType1_R.getLabel()).replace("$", "")
                .equals(atomName)) {
              continue;
            }

            if (newReasonedTuples.get(fieldType_O.getID()) == null) {
              newReasonedTuples.put(fieldType_O.getID(), new ArrayList<>(Arrays.asList(tupleType)));
            } else {
              newReasonedTuples.get(fieldType_O.getID()).add(tupleType);
            }
          }
        }
      }
    }
    return newReasonedTuples;
  }

  private boolean isRequiredPass(final int reasonedRelationCount,
      final Map<Integer, List<TupleType>> newReasonedTuples) {
    boolean requirePass = true;

    if (AlloyOtherSolutionReasoningForAtom.solutions.size() == 1) {
      return false;
    }

    int unMatchedExistingReasoningCount = 0;
    if (reasonedRelationCount > 0) {
      for (int solutionNumber =
          0; solutionNumber < AlloyOtherSolutionReasoningForAtom.reasonedTuples
          .size(); solutionNumber++) {
        if (solutionNumber == AlloyOtherSolutionReasoningForAtom.currentSolutionIndex) {
          continue;
        }
        final Map<Integer, List<TupleType>> existingReasonedTuples =
            AlloyOtherSolutionReasoningForAtom.reasonedTuples.get(solutionNumber);
        for (final Entry<Integer, List<TupleType>> newReasonsedTuplesEntry : newReasonedTuples
            .entrySet()) {
          final Integer fieldId = newReasonsedTuplesEntry.getKey();
          final List<TupleType> tupleList_New = newReasonsedTuplesEntry.getValue();
          if (existingReasonedTuples.containsKey(fieldId)) {
            final List<TupleType> tupleList_Old = existingReasonedTuples.get(fieldId);
            if (tupleList_New.size() != tupleList_Old.size()) {
              unMatchedExistingReasoningCount++;
              break;
            }
            int matchedTupleCount = 0;
            for (final TupleType tupleType_New : tupleList_New) {
              final String atom0Label_New = tupleType_New.getAtom().get(0).getLabel();
              final String atom1Label_New = tupleType_New.getAtom().get(1).getLabel();

              final boolean anyMatchOldTuple = tupleList_Old.stream().anyMatch(
                  tupleType_Old -> tupleType_Old.getAtom().get(0).getLabel().equals(atom0Label_New)
                  && tupleType_Old.getAtom().get(1).getLabel().equals(atom1Label_New));

              if (anyMatchOldTuple) {
                matchedTupleCount++;
              }
            }
            if (matchedTupleCount != tupleList_New.size()) {
              unMatchedExistingReasoningCount++;
              break;
            }
          } else {
            unMatchedExistingReasoningCount++;
            break;
          }
        }
      }
      if (unMatchedExistingReasoningCount == AlloyOtherSolutionReasoningForAtom.reasonedTuples
          .size() - 1) {
        requirePass = false;
      }
    }
    return requirePass;
  }

  private AtomType getOriginalAtomType(final String name_R) {
    if (name_R.contains("/")) {
      return null;
    }
    final String name = name_R.substring(0, name_R.lastIndexOf("_"));
    final int id =
        Integer.parseInt(name_R.substring(name_R.lastIndexOf("_") + 1, name_R.lastIndexOf("$")));

    return AlloyUtilities.getSigTypeById(AlloyUtilities.getSigTypeIdByName(name)).getAtom().get(id);
  }
}
