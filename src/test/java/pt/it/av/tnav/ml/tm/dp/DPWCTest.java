package pt.it.av.tnav.ml.tm.dp;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.it.av.tnav.ml.tm.corpus.Corpus;
import pt.it.av.tnav.ml.tm.corpus.MockCorpus;
import pt.it.av.tnav.ml.tm.dp.cache.DPWPCache;
import pt.it.av.tnav.ml.tm.ngrams.NGram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link DPWC} class.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class DPWCTest {
  private static DPWC car;

  @BeforeClass
  public static void setup() {
    List<DPWC.Category> categories = new ArrayList<>();
    List<DPW.DpDimension> d1 = new ArrayList<>(),
        d2 = new ArrayList<>(),
        d3 = new ArrayList<>();

    d1.add(new DPW.DpDimension(NGram.Unigram("car"), NGram.Unigram("car"), 0.7127072536215101));
    d1.add(new DPW.DpDimension(NGram.Unigram("automobile"), NGram.Unigram("automobil"), 0.6360523692007233));
    d2.add(new DPW.DpDimension(NGram.Unigram("propulsion"), NGram.Unigram("propuls"), 0.2792492378611569));
    d2.add(new DPW.DpDimension(NGram.Unigram("system"), NGram.Unigram("system"), 0.27924979957100793));
    d3.add(new DPW.DpDimension(NGram.Unigram("benz"), NGram.Unigram("benz"), 0.29510880183107174));

    categories.add(new DPWC.Category(d1, 1.0));
    categories.add(new DPWC.Category(d3, 0.36073853237476433));
    categories.add(new DPWC.Category(d2, 0.3413922866664169));
    car = new DPWC(NGram.Unigram("car"), categories);
  }

  @Test
  public void test_learn() {
    HashMap<String, List<String>> map = new HashMap<>();
    List<String> lcar = new ArrayList<>(),
        lautomobile = new ArrayList<>(), lpropulsion = new ArrayList<>(),
        lsystem = new ArrayList<>(), lbenz = new ArrayList<>();
    // car
    lcar.add("A car (or automobile) is a wheeled motor vehicle used for transportation.");
    lcar.add("Most definitions of car say they run primarily on roads, seat one to eight people, have four tires, and mainly transport people rather than goods.");
    lcar.add("Cars came into global use during the 20th century, and developed economies depend on them.");
    lcar.add("The year 1886 is regarded as the birth year of the modern car when German inventor Karl Benz patented his Benz Patent-Motorwagen.");
    lcar.add("One of the first cars that were accessible to the masses was the 1908 Model T, an American car manufactured by the Ford Motor Company.");
    lcar.add("Cars were rapidly adopted in the US, where they replaced animal-drawn carriages and carts, but took much longer to be accepted in Western Europe and other parts of the world.");
    lcar.add("An automobile is a car, driven by an operator, with its own propulsion system, whereas most cars are attached to another propulsion system (locomotive, for example).");
    lcar.add("Automobile and car are essentially interchangeable though.");
    lcar.add("The British ones say car and almost everywhere else I can find seems to agrre that automobile is the same as car.");
    // motor
    lautomobile.add("Automobile (or car) – wheeled passenger vehicle that carries its own motor.");
    lautomobile.add("Most definitions of the term specify that automobiles are designed to run primarily on roads, to have seating for one to six people, typically have four wheels and be constructed principally for the transport of people rather than goods.");
    lautomobile.add("Automobiles (or cars) are passenger vehicles designed for operation on ordinary roads and typically having four wheels and a gasoline or diesel internal-combustion engine.");
    lautomobile.add("An automobile is a car: a vehicle with four wheels and an internal combustion engine.");
    lautomobile.add("Trucks, vans, buses, and limousines are bigger than the typical automobile (or car), but they're automobiles, too.");
    lautomobile.add("A motorcycle isn't an automobile (or car) because it only has two wheels.");
    // propulsion
    lpropulsion.add("Propulsion means to push forward or drive an object forward. The term is derived from two Latin words: pro, meaning before or forward; and pellere, meaning to drive.");
    lpropulsion.add("A propulsion system consists of a source of mechanical power, and a propulsor.");
    lpropulsion.add("A propulsion system is a machine that produces thrust to push an object forward.");
    lpropulsion.add("On airplanes, thrust is usually generated through some application of Newton's third law of action and reaction.");
    lpropulsion.add("A gas, or working fluid, is accelerated by the engine, and the reaction to this acceleration produces a force on the engine.");
    lpropulsion.add("A propulsion system has a source of mechanical power (some type of engine or motor, muscles), and some means of using this power to generate force, such as wheel and axles, propellers, a propulsive nozzle, wings, fins or legs.");
    lpropulsion.add("Other components such as clutches, gearboxes and so forth may be needed to connect the power source to the force generating component.");
    lpropulsion.add("The term propulsion is derived from two Latin words: pro meaning before or forwards and pellere meaning to drive.");
    // system
    lsystem.add("A system is a regularly interacting or interdependent group of units forming an integrated whole.");
    lsystem.add("Every system is delineated by its spatial and temporal boundaries, surrounded and influenced by its environment, described by its structure and purpose and expressed in its functioning.");
    lsystem.add("A system is a general set of parts, steps, or components that are connected to form a more complex whole. For example, a computer system contains processors, memory, electrical pathways, a power supply, etc.");
    lsystem.add("For a very different example, a business is a system made up of methods, procedures, and routines.");
    // benz
    lbenz.add("Mercedes-Benz is a global automobile marque and a division of the German company Daimler AG.");
    lbenz.add("The brand is known for luxury vehicles, buses, coaches, and lorries.");
    lbenz.add("The headquarters is in Stuttgart, Baden-Württemberg.");
    lbenz.add("The name first appeared in 1926 under Daimler-Benz.");
    lbenz.add("Mercedes-Benz traces its origins to Daimler-Motoren-Gesellschaft's 1901 Mercedes and Karl Benz's 1886 Benz Patent-Motorwagen, which is widely regarded as the first gasoline-powered automobile.");
    lbenz.add("The slogan for the brand is \"the best or nothing\".");
    map.put("car", lcar);
    map.put("automobile", lautomobile);
    map.put("propulsion", lpropulsion);
    map.put("system", lsystem);
    map.put("benz", lbenz);
    Corpus c = new MockCorpus(map);
    String tmp = System.getProperty("java.io.tmpdir");
    DPWPCache cache = new DPWPCache(Paths.get(tmp), c);
    DPWC dpwc = DPWC.learn(NGram.Unigram("car"), cache);
    assertEquals(car,dpwc);
    try {
      Files.deleteIfExists(Paths.get(tmp).resolve("car.gz"));
      Files.deleteIfExists(Paths.get(tmp).resolve("automobile.gz"));
      Files.deleteIfExists(Paths.get(tmp).resolve("propulsion.gz"));
      Files.deleteIfExists(Paths.get(tmp).resolve("system.gz"));
      Files.deleteIfExists(Paths.get(tmp).resolve("benz.gz"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
