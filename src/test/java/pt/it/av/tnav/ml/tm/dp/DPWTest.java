package pt.it.av.tnav.ml.tm.dp;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.it.av.tnav.ml.tm.corpus.Corpus;
import pt.it.av.tnav.ml.tm.corpus.MockCorpus;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link DPW} class.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWTest {
  private static DPW dpw1, dpw2, car;

  @BeforeClass
  public static void setup() {
    List<DPW.DpDimension> p1 = new ArrayList<>(), p2 = new ArrayList<>(), p3 = new ArrayList<>();
    p1.add(new DPW.DpDimension(NGram.Unigram("engine"), NGram.Unigram("engine"), 6.0));
    p1.add(new DPW.DpDimension(NGram.Unigram("wheels"), NGram.Unigram("wheels"), 4.0));
    p1.add(new DPW.DpDimension(NGram.Unigram("door"), NGram.Unigram("door"), 3.0));
    dpw1 = new DPW(NGram.Unigram("car"), p1);
    p2.add(new DPW.DpDimension(NGram.Unigram("engine"), NGram.Unigram("engine"), 4.0));
    p2.add(new DPW.DpDimension(NGram.Unigram("wheels"), NGram.Unigram("wheels"), 6.0));
    p2.add(new DPW.DpDimension(NGram.Unigram("driver"), NGram.Unigram("driver"), 4.0));
    dpw2 = new DPW(NGram.Unigram("bike"), p2);
    p3.add(new DPW.DpDimension(NGram.Unigram("car"), NGram.Unigram("car"), 12.0));
    p3.add(new DPW.DpDimension(NGram.Unigram("automobile"), NGram.Unigram("automobil"), 6.0));
    p3.add(new DPW.DpDimension(NGram.Unigram("propulsion"), NGram.Unigram("propuls"), 5.0));
    p3.add(new DPW.DpDimension(NGram.Unigram("system"), NGram.Unigram("system"), 5.0));
    p3.add(new DPW.DpDimension(NGram.Unigram("benz"), NGram.Unigram("benz"), 5.0));
    car = new DPW(NGram.Unigram("car"), p3);
  }

  @Test
  public void test_similarity() {
    assertEquals(1.0, dpw1.similarityTo(dpw1),0.01);
    assertEquals(1.0, dpw2.similarityTo(dpw2),0.01);
    assertEquals(0.7452841128534441, dpw1.similarityTo(dpw2),0.01);
    assertEquals(0.7452841128534441, dpw2.similarityTo(dpw1),0.01);
  }

  @Test
  public void test_getCoor() {
    assertEquals(0.0, dpw1.dimention(NGram.Unigram("banana")), 0.01);
    assertEquals(6.0, dpw1.dimention(NGram.Unigram("engine")), 0.01);
    assertEquals(4.0, dpw1.dimention(NGram.Unigram("wheels")), 0.01);
    assertEquals(3.0, dpw1.dimention(NGram.Unigram("door")), 0.01);
    assertEquals(0.0, dpw2.dimention(NGram.Unigram("banana")), 0.01);
    assertEquals(4.0, dpw2.dimention(NGram.Unigram("engine")), 0.01);
    assertEquals(6.0, dpw2.dimention(NGram.Unigram("wheels")), 0.01);
    assertEquals(4.0, dpw2.dimention(NGram.Unigram("driver")), 0.01);
  }

  @Test
  public void test_learn() {
    HashMap<String, List<String>> map = new HashMap<>();
    List<String> lcar = new ArrayList<>();
    lcar.add("A car (or automobile) is a wheeled motor vehicle used for transportation.");
    lcar.add("Most definitions of car say they run primarily on roads, seat one to eight people, have four tires, and mainly transport people rather than goods.");
    lcar.add("Cars came into global use during the 20th century, and developed economies depend on them.");
    lcar.add("The year 1886 is regarded as the birth year of the modern car when German inventor Karl Benz patented his Benz Patent-Motorwagen.");
    lcar.add("One of the first cars that were accessible to the masses was the 1908 Model T, an American car manufactured by the Ford Motor Company.");
    lcar.add("Cars were rapidly adopted in the US, where they replaced animal-drawn carriages and carts, but took much longer to be accepted in Western Europe and other parts of the world.");
    lcar.add("An automobile is a car, driven by an operator, with its own propulsion system, whereas most cars are attached to another propulsion system (locomotive, for example).");
    lcar.add("Automobile and car are essentially interchangeable though.");
    lcar.add("The British ones say car and almost everywhere else I can find seems to agrre that automobile is the same as car.");
    map.put("car", lcar);
    Corpus c = new MockCorpus(map);
    DPW dpw = DPW.learn(NGram.Unigram("car"), c);
    assertEquals(car,dpw);
  }
}
