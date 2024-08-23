
  "GitHubRepoController.deleteDirectoryOrFile" should {
    " return 400 there is error in the Form " in {
      val invalidFormData = Map("message" -> "", "sha" -> "nonEmptySha")
      val userName = "testUser"
      val repoName = "testRepo"
      val path = "testPath"
      val fileName = "testFileName"
      val request = FakeRequest(GET, s"/GitHub/repos/$userName/$repoName/$path/$fileName")
        .withFormUrlEncodedBody(invalidFormData.toSeq: _*)
      val result = testGitHubRepoController.deleteDirectoryOrFile(userName, repoName, path, fileName)(request)
      status(result) mustBe BAD_REQUEST


    }
    " return 200 when the file is successfully deleted " in {
      // Mock the gitService.deleteDirectoryOrFile method to return a successful result
      val userName = "user"
      val repoName = "repo"
      val path = "path"
      val fileName = "file.txt"

      val formData = DeleteModel("Commit message", "someSha")
      when(mockGitHubServices.deleteDirectoryOrFile(any(), any(), any(), eqTo(formData))(any()))
        .thenReturn(EitherT.rightT("File deleted"))
      // Simulate a valid form submission
      implicit val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest(GET, s"/GitHub/repos/$userName/$repoName/$path/$fileName").withFormUrlEncodedBody("message" -> "Commit message", "sha" -> "someSha")
      val boundForm = deleteForm.bindFromRequest()
      boundForm.hasErrors mustBe false
      val result: Future[Result] = testGitHubRepoController.deleteDirectoryOrFile(userName, repoName,path, fileName)(request)

      // Assert the status and content of the response
      status(result) mustBe CREATED

    }