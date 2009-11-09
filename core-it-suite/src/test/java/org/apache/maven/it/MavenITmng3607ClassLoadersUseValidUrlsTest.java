package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.net.URI;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-3607">MNG-3607</a>.
 * 
 * @author Benjamin Bentmann
 * @version $Id$
 */
public class MavenITmng3607ClassLoadersUseValidUrlsTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng3607ClassLoadersUseValidUrlsTest()
    {
        super( "[3.0-alpha-2,)" );
    }

    /**
     * Test that class loaders created by Maven employ valid URLs, e.g. properly encode characters like spaces.
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3607" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.getSystemProperties().setProperty( "test.home", testDir.getAbsolutePath() );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/pcl.properties" );
        String url = props.getProperty( "maven-core-it.properties" );
        assertNotNull( url );
        assertNotNull( new URI( url ) );
        assertTrue( url.indexOf( ' ' ) < 0 );
    }

}