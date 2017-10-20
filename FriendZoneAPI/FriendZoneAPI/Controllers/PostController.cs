using FriendZoneAPI.Models;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Web.Http;

namespace FriendZoneAPI.Controllers
{
    public class PostController : ApiController
    {
        FriendZoneEntities database = new FriendZoneEntities();

        // GET api/post
        public List<Post> Get()
        {
            List<Post> posts = database.Post.ToList();
            return posts;
        }

        // GET api/post/id
        public Post Get(int id)
        {
            Post post = database.Post.Find(id);
            return post;
        }

        // GET api/post
        public List<Post> Get(int start, int count)
        {
            List<Post> posts = database.Post.OrderByDescending(p => p.ID).ToList();
            posts.RemoveRange(0, start);
            if (count < posts.Count)
            {
                posts.RemoveRange(count, posts.Count);
            }
            return posts;
        }

        // POST api/post
        public void Post([FromBody]Post post)
        {
            database.Post.Add(post);
            database.SaveChanges();
        }

        // PUT api/post/id
        public void Put(int id, [FromBody]Post newPost)
        {
            Post oldPost = database.Post.Find(id);
            oldPost.Description = newPost.Description;
            database.SaveChanges();
        }

        // DELETE api/post/id
        public void Delete(int id)
        {
            database.Post.Remove(database.Post.SingleOrDefault(p => p.ID.Equals(id)));
            database.SaveChanges();
        }
    }
}
