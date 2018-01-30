package org.openflexo.jgitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class RepositoryTest {

	private static Repository gitRepository;

	private static Git git;

	@BeforeClass
	public static void initializeRepositoryGit() throws IOException, IllegalStateException, GitAPIException {

		File tempFile = File.createTempFile("Temp", "");
		File dir = new File(tempFile.getParentFile(), tempFile.getName() + "TestGitRepository");
		tempFile.delete();

		System.out.println("Working with dir=" + dir);

		File gitDir = new File(dir.getAbsolutePath(), ".git");
		Git.init().setDirectory(dir).call();

		// Our GitRepository is of type file
		gitRepository = FileRepositoryBuilder.create(gitDir);
		System.out.println("Created a new repository at " + gitRepository.getDirectory());
		// Where files are checked
		System.out.println("Working tree : " + gitRepository.getWorkTree());
		// Where index is checked
		System.out.println("Index File : " + gitRepository.getIndexFile());
		git = new Git(gitRepository);

		System.out.println("New Git command created");

	}

	@Test
	@TestOrder(1)
	public void addFiletoIndexInRepository() throws NoFilepatternException, GitAPIException, IOException {
		// Prepare
		File fileToAdd = new File(gitRepository.getDirectory().getParent(), "fileToAdd.txt");
		fileToAdd.createNewFile();
		// run the add-call

		// Execute
		git.add().addFilepattern("fileToAdd.txt").call();
		System.out.println("Added file " + fileToAdd + " to repository at " + gitRepository.getDirectory());

		System.out.println("Cache Entry : " + DirCache.read(gitRepository).getEntry("fileToAdd.txt").getPathString());
		// Verify

		// Check if the file has been added to the index
		assertEquals(fileToAdd.getName(), DirCache.read(gitRepository).getEntry("fileToAdd.txt").getPathString());
	}

	@Test
	@TestOrder(2)
	public void commitFileInRepository() throws IOException, NoFilepatternException, GitAPIException {
		// Prepare
		File fileToAdd = new File(gitRepository.getDirectory().getParent(), "fileToCommit.txt");
		fileToAdd.createNewFile();
		// run the add-call
		git.add().addFilepattern("fileToCommit.txt").call();
		// Execute
		git.commit().setMessage("Added fileToCommit").call();

		System.out.println("Committed file " + fileToAdd + " to repository at " + gitRepository.getDirectory());

		// Verify
		Status status = git.status().call();
		// Has been commited
		System.out.println("uncommited changes = " + status.getUncommittedChanges());
		assertEquals(status.getUncommittedChanges().size(), 0);
	}

	@Test
	@TestOrder(3)
	public void createBranch() throws RefAlreadyExistsException, RefNotFoundException, GitAPIException, GitAPIException, IOException {
		git.branchCreate().setName("MyGitBranch").call();
		System.out.println("Current Branch :" + gitRepository.getBranch());
		git.checkout().setName("MyGitBranch").call();
		System.out.println("Current Branch after checkout : " + gitRepository.getBranch());
		assertEquals(gitRepository.getBranch(), "MyGitBranch");

	}

	@Test
	@TestOrder(4)
	public void listObjectsOfACommit() throws IOException, NoFilepatternException, GitAPIException {
		File fileToAdd = new File(gitRepository.getDirectory().getParent(), "fileToCommit.txt");
		fileToAdd.createNewFile();
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileToAdd)))) {
			writer.println("TestCommit");
			writer.flush();
		}
		// run the add-call
		git.add().addFilepattern("fileToCommit.txt").call();

		git.commit().setMessage("Added fileToCommit").call();

		ObjectId lastCommitId = gitRepository.resolve(Constants.HEAD);

		System.out.println("Commit id : " + lastCommitId.getName());

		// Allows us to walk through a commit

		try (RevWalk walkInLastCommit = new RevWalk(gitRepository)) {

			// Get the commit object from the commit Id
			RevCommit lastCommit = walkInLastCommit.parseCommit(lastCommitId);

			try (TreeWalk treeWalk = new TreeWalk(gitRepository)) {
				treeWalk.addTree(lastCommit.getTree());
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("fileToCommit.txt"));
				if (!treeWalk.next()) {
					System.out.println("Couldn't find file");
				}
				else {
					System.out.println("Found : " + treeWalk.getPathString());
				}
				ObjectId objectId = treeWalk.getObjectId(0);

				System.out.println("Object Id : " + objectId.getName());
				ObjectLoader loader = gitRepository.open(objectId);

				loader.copyTo(System.out);
			}
		}
	}

	@Test
	@TestOrder(5)
	public void showUncommitedFiles() throws IOException, NoWorkTreeException, GitAPIException {

		// Prepare
		Status status = git.status().call();

		System.out.println("Before modification clean : " + status.isClean());

		File file = new File(gitRepository.getWorkTree(), "fileModified.txt");
		file.createNewFile();
		git.add().addFilepattern("fileModified.txt").call();
		git.commit().setMessage("Added fileModified").call();

		// Execute

		FileUtils.write(file, new Date().toString());

		// refresh git Status
		status = git.status().call();

		System.out.println("After modification clean : " + status.isClean());
		// Verify
		Set<String> modified = status.getModified();
		for (String modify : modified) {
			System.out.println("Modification fileModified: " + modify);
		}

		Set<String> uncommittedChanges = status.getUncommittedChanges();
		for (String uncommitted : uncommittedChanges) {
			System.out.println("Uncommitted: " + uncommitted);
		}
		assertNotNull(modified);
		assertNotNull(uncommittedChanges);
	}

	@Test
	@TestOrder(6)
	public void getSha1ObjectId() throws IOException, NoFilepatternException, GitAPIException {
		File fileToAdd = new File(gitRepository.getDirectory().getParent(), "fileToCommit.txt");
		fileToAdd.createNewFile();
		// run the add-call
		git.add().addFilepattern("fileToCommit.txt").call();

		System.out.println("Added file " + fileToAdd + " to repository at " + gitRepository.getDirectory());

		git.commit().setMessage("Added fileToCommit").call();

		System.out.println("Committed file " + fileToAdd + " to repository at " + gitRepository.getDirectory());

	}

	@Test
	@TestOrder(7)
	public void sameIdBetweenAddAndCommit() throws IOException, NoFilepatternException, GitAPIException {
		// Prepare
		File fileToCompare = new File(gitRepository.getDirectory().getParent(), "fileToCompare.txt");
		fileToCompare.createNewFile();
		// run the add-call
		DirCache addedInCache = git.add().addFilepattern("fileToCompare.txt").call();

		ObjectId idFileToRetrieve = addedInCache.getEntry("fileToCompare.txt").getObjectId();

		RevCommit commit = git.commit().setMessage("Added fileToCompare").call();

		try (RevWalk walkInLastCommit = new RevWalk(gitRepository)) {

			// Get the commit object from the commit Id
			RevCommit lastCommit = walkInLastCommit.parseCommit(commit.getId());

			try (TreeWalk treeWalk = new TreeWalk(gitRepository)) {
				treeWalk.addTree(lastCommit.getTree());
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("fileToCompare.txt"));
				if (!treeWalk.next()) {
					System.out.println("Couldn't find file");
				}
				else {
					System.out.println("Found : " + treeWalk.getPathString());
				}
				ObjectId objectId = treeWalk.getObjectId(0);
				System.out.println("Object Id : " + objectId.getName());

				// Verify
				// Check that the id after add and after commit is the same for git
				assertEquals(objectId, idFileToRetrieve);
			}
		}
	}

	@Test
	@TestOrder(8)
	public void loadExistingFile() throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException,
			NoFilepatternException, GitAPIException {
		File fileToRetrieve = new File(gitRepository.getDirectory().getParent(), "fileToRetrieve.txt");
		fileToRetrieve.createNewFile();

		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileToRetrieve)))) {
			writer.println("aaaa");
			writer.flush();
		}
		// run the add-call
		DirCache addedInCache = git.add().addFilepattern("fileToRetrieve.txt").call();
		ObjectId idFileToRetrieve = addedInCache.getEntry("fileToRetrieve.txt").getObjectId();

		// Unused ObjectId commitId =
		git.commit().setMessage("Commit FileToRetrieve").call().getId();

		FileTreeIterator fileTree = new FileTreeIterator(gitRepository);
		while (!fileTree.getEntryObjectId().equals(idFileToRetrieve)) {
			fileTree.next(1);
		}
		System.out.println("File retrieved :" + fileTree.getEntryFile().getName());
		assertEquals(fileTree.getEntryFile().getName(), "fileToRetrieve.txt");
	}

	@AfterClass
	public static void deleteGitRepository() throws IOException {
		gitRepository.close();

		// Clean up the git folder
		File directoryToDelete = gitRepository.getDirectory().getParentFile();
		if (directoryToDelete.isDirectory()) {
			FileUtils.cleanDirectory(directoryToDelete);
		}
	}

}
