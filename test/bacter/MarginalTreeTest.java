/*
 * Copyright (C) 2014 Tim Vaughan <tgvaughan@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bacter;

import bacter.ConversionGraph;
import bacter.Conversion;
import beast.util.TreeParser;
import java.util.Set;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for marginal tree traversal.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class MarginalTreeTest extends TestBase {
    
    public MarginalTreeTest() {
    }

    @Test
    public void test() throws Exception {

        // Conversion graph
        String str = "[&2,2759,0.3260126313706676,10,2808,0.42839862922656696] "
                + "[&10,6692,0.3381366423491633,2,6693,0.5683827224649434] "
                + "[&10,8069,0.2807615297583804,14,8160,0.3415740002783274] "
                + "[&2,9000,0.1,2,9500,0.4] "
                + "(((0:0.04916909893812008,1:0.04916909893812008)10:0.5465237639426681,"
                + "(4:0.3773111326866937,(((8:0.22180790639747835,"
                + "(3:0.07561592852503513,6:0.07561592852503513)11:0.14619197787244323)"
                + "13:0.010206467073885589,9:0.23201437347136394)14:0.116542689187905,"
                + "(7:0.10746702934931932,5:0.10746702934931932)12:0.24109003330994963)"
                + "15:0.02875407002742475)16:0.21838173019409446)17:1.1073878800617445,"
                + "2:1.7030807429425328)18:0.0";
        
        ConversionGraph arg = new ConversionGraph();
        arg.initByName("fromString", str, "sequenceLength", 10000);
        System.out.println(arg.getExtendedNewick(true));
        
        // Test all marginals against truth
        String[] correctNewickStrings = {
            "(((0:0.04916909893812016,1:0.04916909893812016)10:0.5465237639426681,(((((3:0.07561592852503529,6:0.07561592852503529)11:0.1461919778724432,8:0.2218079063974785)13:0.010206467073885506,9:0.232014373471364)14:0.11654268918790511,(5:0.1074670293493194,7:0.1074670293493194)12:0.24109003330994971)15:0.028754070027424694,4:0.3773111326866938)16:0.21838173019409446)17:1.1073878800617445,2:1.7030807429425328)18:0.0",
            "((2:0.42839862922656696,(0:0.04916909893812016,1:0.04916909893812016)10:0.3792295302884468)18:0.1672942336542213,(((((3:0.07561592852503529,6:0.07561592852503529)11:0.1461919778724432,8:0.2218079063974785)13:0.010206467073885506,9:0.232014373471364)14:0.11654268918790511,(5:0.1074670293493194,7:0.1074670293493194)12:0.24109003330994971)15:0.028754070027424694,4:0.3773111326866938)16:0.21838173019409446)17:0.0",
            "((((((3:0.07561592852503529,6:0.07561592852503529)11:0.1461919778724432,8:0.2218079063974785)13:0.010206467073885506,9:0.232014373471364)14:0.11654268918790511,(5:0.1074670293493194,7:0.1074670293493194)12:0.24109003330994971)15:0.028754070027424694,4:0.3773111326866938)16:1.325769610255839,((0:0.04916909893812016,1:0.04916909893812016)10:0.5192136235268232,2:0.5683827224649434)17:1.1346980204775894)18:0.0",
            "(((((0:0.04916909893812016,1:0.04916909893812016)10:0.29240490134020725,(((3:0.07561592852503529,6:0.07561592852503529)11:0.1461919778724432,8:0.2218079063974785)13:0.010206467073885506,9:0.232014373471364)14:0.1095596268069634)17:0.006983062380941707,(5:0.1074670293493194,7:0.1074670293493194)12:0.24109003330994971)15:0.028754070027424694,4:0.3773111326866938)16:1.325769610255839,2:1.7030807429425328)18:0.0",
            "(((0:0.04916909893812016,1:0.04916909893812016)10:0.5465237639426681,(((((3:0.07561592852503529,6:0.07561592852503529)11:0.1461919778724432,8:0.2218079063974785)13:0.010206467073885506,9:0.232014373471364)14:0.11654268918790511,(5:0.1074670293493194,7:0.1074670293493194)12:0.24109003330994971)15:0.028754070027424694,4:0.3773111326866938)16:0.21838173019409446)17:1.1073878800617445,2:1.7030807429425328)18:0.0"
        };

        RegionList regionList = new RegionList(arg);

        for (Region region : regionList.getRegions()) {
            System.out.println(new MarginalTree(arg, region) + ";");
        }

        for (int r=0; r<arg.getConversions().size(); r++) {
            Conversion recomb = arg.getConversions().get(r);
            
            assertTrue(treesEquivalent(
                new MarginalTree(arg, regionList.getRegions().get(r)).getRoot(),
                new TreeParser(correctNewickStrings[r], false, true, false, 0).getRoot(),
                1e-15)); 
        }
    }
}
